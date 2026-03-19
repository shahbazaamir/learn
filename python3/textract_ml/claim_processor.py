"""
Medical Insurance Claim Processor — AWS Textract
Use case: Extract structured data from uploaded claim forms (PDFs/images).

Advantages demonstrated:
- No ML expertise: managed Textract API
- Beyond OCR: form key-value + table extraction
- Handles complex layouts: multi-column forms, checkboxes, handwriting
- Async processing: StartDocumentAnalysis for multi-page PDFs
- Native AWS integration: S3 trigger, SNS notification, A2I human review
- Confidence filtering: flags low-confidence fields for review
- Structured output: returns typed claim dict with confidence scores
"""

import json
import time
import boto3

textract  = boto3.client("textract")
sns       = boto3.client("sns")
s3        = boto3.client("s3")
a2i       = boto3.client("sagemaker-a2i-runtime")

SNS_TOPIC_ARN    = "arn:aws:sns:us-east-1:<account-id>:ClaimProcessed"
A2I_FLOW_ARN     = "arn:aws:sagemaker:us-east-1:<account-id>:flow-definition/claim-review"
CONFIDENCE_THRESHOLD = 80.0  # fields below this go to human review


# ── 1. Start async Textract job (handles large/multi-page PDFs) ──────────────

def start_extraction(bucket: str, key: str) -> str:
    response = textract.start_document_analysis(
        DocumentLocation={"S3Object": {"Bucket": bucket, "Name": key}},
        FeatureTypes=["FORMS", "TABLES"],
    )
    return response["JobId"]


# ── 2. Poll until job completes ───────────────────────────────────────────────

def wait_for_job(job_id: str) -> list:
    blocks, next_token = [], None
    while True:
        kwargs = {"JobId": job_id}
        if next_token:
            kwargs["NextToken"] = next_token
        resp = textract.get_document_analysis(**kwargs)
        status = resp["JobStatus"]
        if status == "FAILED":
            raise RuntimeError(f"Textract job failed: {resp.get('StatusMessage')}")
        if status == "SUCCEEDED":
            blocks.extend(resp["Blocks"])
            next_token = resp.get("NextToken")
            if not next_token:
                break
        else:
            time.sleep(5)
    return blocks


# ── 3. Parse key-value pairs with confidence scores ──────────────────────────

def parse_kv(blocks: list) -> dict[str, dict]:
    block_map  = {b["Id"]: b for b in blocks}
    key_blocks = [
        b for b in blocks
        if b["BlockType"] == "KEY_VALUE_SET" and "KEY" in b.get("EntityTypes", [])
    ]
    fields = {}
    for kb in key_blocks:
        key_text  = _get_text(kb, block_map)
        val_text, val_conf = "", kb.get("Confidence", 0)
        for rel in kb.get("Relationships", []):
            if rel["Type"] == "VALUE":
                for vid in rel["Ids"]:
                    vb = block_map[vid]
                    val_text = _get_text(vb, block_map)
                    val_conf = min(val_conf, vb.get("Confidence", 0))
        fields[key_text] = {"value": val_text, "confidence": round(val_conf, 2)}
    return fields


def _get_text(block: dict, block_map: dict) -> str:
    words = []
    for rel in block.get("Relationships", []):
        if rel["Type"] == "CHILD":
            for cid in rel["Ids"]:
                child = block_map.get(cid, {})
                if child.get("BlockType") == "WORD":
                    words.append(child.get("Text", ""))
    return " ".join(words)


# ── 4. Parse tables (e.g. itemised medical procedures) ───────────────────────

def parse_tables(blocks: list) -> list[list[str]]:
    block_map = {b["Id"]: b for b in blocks}
    tables, cells = [], {}
    for b in blocks:
        if b["BlockType"] == "TABLE":
            table_cells = {}
            for rel in b.get("Relationships", []):
                if rel["Type"] == "CHILD":
                    for cid in rel["Ids"]:
                        cell = block_map[cid]
                        if cell["BlockType"] == "CELL":
                            r, c = cell["RowIndex"], cell["ColumnIndex"]
                            table_cells[(r, c)] = _get_text(cell, block_map)
            if table_cells:
                max_r = max(k[0] for k in table_cells)
                max_c = max(k[1] for k in table_cells)
                tables.append([
                    [table_cells.get((r, c), "") for c in range(1, max_c + 1)]
                    for r in range(1, max_r + 1)
                ])
    return tables


# ── 5. Flag low-confidence fields for A2I human review ───────────────────────

def maybe_send_to_human_review(claim_id: str, fields: dict, bucket: str, key: str):
    low_conf = {k: v for k, v in fields.items() if v["confidence"] < CONFIDENCE_THRESHOLD}
    if not low_conf:
        return False
    a2i.start_human_loop(
        HumanLoopName=f"claim-{claim_id}",
        FlowDefinitionArn=A2I_FLOW_ARN,
        HumanLoopInput={
            "InputContent": json.dumps({
                "claim_id": claim_id,
                "s3_key": key,
                "low_confidence_fields": low_conf,
            })
        },
    )
    return True


# ── 6. Notify downstream via SNS ─────────────────────────────────────────────

def notify(claim: dict):
    sns.publish(
        TopicArn=SNS_TOPIC_ARN,
        Subject="Claim Processed",
        Message=json.dumps(claim, indent=2),
    )


# ── Lambda handler ────────────────────────────────────────────────────────────

def lambda_handler(event, context):
    bucket = event["Records"][0]["s3"]["bucket"]["name"]
    key    = event["Records"][0]["s3"]["object"]["key"]
    claim_id = key.split("/")[-1].split(".")[0]  # e.g. "claims/CLM-001.pdf" → "CLM-001"

    # Async extraction (handles multi-page PDFs without Lambda timeout)
    job_id = start_extraction(bucket, key)
    blocks = wait_for_job(job_id)

    # Structured extraction
    fields = parse_kv(blocks)
    tables = parse_tables(blocks)

    claim = {
        "claim_id": claim_id,
        "fields":   fields,
        "tables":   tables,
    }

    # Confidence-based routing
    needs_review = maybe_send_to_human_review(claim_id, fields, bucket, key)
    claim["status"] = "PENDING_REVIEW" if needs_review else "AUTO_APPROVED"

    notify(claim)
    print(json.dumps(claim, indent=2))

    return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, "status": claim["status"]})}
