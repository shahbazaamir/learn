"""
Lambda function: triggered by S3 upload, extracts text via Textract,
returns structured key-value pairs from the document.
"""

import json
import boto3

textract = boto3.client("textract")


def extract_kv(blocks: list) -> dict:
    # Map block ids for lookup
    block_map = {b["Id"]: b for b in blocks}
    key_blocks = [b for b in blocks if b["BlockType"] == "KEY_VALUE_SET" and "KEY" in b.get("EntityTypes", [])]

    result = {}
    for key_block in key_blocks:
        key_text = get_text(key_block, block_map)
        val_text = ""
        for rel in key_block.get("Relationships", []):
            if rel["Type"] == "VALUE":
                for vid in rel["Ids"]:
                    val_text = get_text(block_map[vid], block_map)
        result[key_text] = val_text
    return result


def get_text(block: dict, block_map: dict) -> str:
    text = ""
    for rel in block.get("Relationships", []):
        if rel["Type"] == "CHILD":
            for cid in rel["Ids"]:
                child = block_map.get(cid, {})
                if child.get("BlockType") == "WORD":
                    text += child.get("Text", "") + " "
    return text.strip()


def lambda_handler(event, context):
    # Get bucket and key from S3 trigger event
    bucket = event["Records"][0]["s3"]["bucket"]["name"]
    key    = event["Records"][0]["s3"]["object"]["key"]

    response = textract.analyze_document(
        Document={"S3Object": {"Bucket": bucket, "Name": key}},
        FeatureTypes=["FORMS"],
    )

    extracted = extract_kv(response["Blocks"])

    print("Extracted fields:", json.dumps(extracted, indent=2))

    return {
        "statusCode": 200,
        "body": json.dumps(extracted),
    }
