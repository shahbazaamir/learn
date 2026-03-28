"""
Step 2 — Claim Adjudication Engine
Use case: After Textract extracts structured data from a claim PDF,
this Lambda consumes the SNS event, validates the claim against
policy rules, enriches it with provider data from DynamoDB,
calculates the approved payout, and writes the decision to RDS + DynamoDB.

Triggered by: SNS topic "ClaimProcessed" (published by claim_processor.py)
"""

import json
import os
import boto3
import urllib.request
from datetime import datetime, date
from decimal import Decimal

dynamodb = boto3.resource("dynamodb")
rds_data = boto3.client("rds-data")

POLICY_TABLE     = os.environ["POLICY_TABLE"]       # DynamoDB: policy details
PROVIDER_TABLE   = os.environ["PROVIDER_TABLE"]     # DynamoDB: approved providers
CLAIMS_TABLE     = os.environ["CLAIMS_TABLE"]       # DynamoDB: claim decisions (fast read)
RDS_ARN          = os.environ["RDS_ARN"]            # Aurora Serverless cluster ARN
RDS_SECRET_ARN   = os.environ["RDS_SECRET_ARN"]     # Secrets Manager ARN for RDS creds
RDS_DATABASE     = os.environ["RDS_DATABASE"]       # e.g. "claims_db"


# ── 1. Load policy for this patient ──────────────────────────────────────────

def get_policy(policy_number: str) -> dict:
    table = dynamodb.Table(POLICY_TABLE)
    resp  = table.get_item(Key={"policy_number": policy_number})
    if "Item" not in resp:
        raise ValueError(f"Policy not found: {policy_number}")
    return resp["Item"]


# ── 2. Validate claim fields against policy rules ────────────────────────────

def validate(fields: dict, policy: dict) -> list[str]:
    """Returns list of rejection reasons. Empty list = valid."""
    errors = []

    claim_date = datetime.strptime(fields.get("Claim Date", {}).get("value", ""), "%d/%m/%Y").date()
    policy_start = datetime.strptime(policy["start_date"], "%Y-%m-%d").date()
    policy_end   = datetime.strptime(policy["end_date"],   "%Y-%m-%d").date()

    if not (policy_start <= claim_date <= policy_end):
        errors.append("CLAIM_DATE_OUTSIDE_POLICY_PERIOD")

    dob_str = fields.get("Date of Birth", {}).get("value", "")
    if dob_str:
        dob = datetime.strptime(dob_str, "%d/%m/%Y").date()
        age = (date.today() - dob).days // 365
        if age < int(policy.get("min_age", 0)) or age > int(policy.get("max_age", 120)):
            errors.append("PATIENT_AGE_NOT_COVERED")

    diagnosis = fields.get("Diagnosis Code", {}).get("value", "")
    excluded  = policy.get("excluded_diagnosis_codes", [])
    if diagnosis in excluded:
        errors.append(f"DIAGNOSIS_NOT_COVERED: {diagnosis}")

    return errors


# ── 3. Verify hospital is an approved network provider ───────────────────────

def check_provider(hospital_name: str) -> bool:
    table = dynamodb.Table(PROVIDER_TABLE)
    resp  = table.get_item(Key={"hospital_name": hospital_name})
    return "Item" in resp


# ── 4. Calculate approved payout from itemised procedures ────────────────────

def calculate_payout(tables: list, policy: dict) -> dict:
    coverage_pct  = Decimal(str(policy.get("coverage_percentage", 80))) / 100
    max_payout    = Decimal(str(policy.get("max_payout", 10000)))
    deductible    = Decimal(str(policy.get("deductible", 500)))

    total_billed  = Decimal("0")
    line_items    = []

    for table in tables:
        for row in table[1:]:  # skip header row
            if len(row) < 4:
                continue
            try:
                cost = Decimal(row[3].replace("$", "").replace(",", ""))
                approved = cost * coverage_pct
                line_items.append({
                    "procedure": row[0],
                    "code":      row[1],
                    "billed":    str(cost),
                    "approved":  str(approved.quantize(Decimal("0.01"))),
                })
                total_billed += cost
            except Exception:
                continue

    gross_payout  = total_billed * coverage_pct
    net_payout    = max(Decimal("0"), min(gross_payout - deductible, max_payout))

    return {
        "total_billed":  str(total_billed),
        "gross_payout":  str(gross_payout.quantize(Decimal("0.01"))),
        "deductible":    str(deductible),
        "net_payout":    str(net_payout.quantize(Decimal("0.01"))),
        "line_items":    line_items,
    }


# ── 5. Persist decision to Aurora (audit trail) + DynamoDB (fast lookup) ─────

def persist_decision(claim_id: str, decision: dict):
    # DynamoDB — real-time status lookup by claim_id
    dynamodb.Table(CLAIMS_TABLE).put_item(Item={
        "claim_id":   claim_id,
        "status":     decision["status"],
        "net_payout": decision["payout"]["net_payout"],
        "decided_at": datetime.utcnow().isoformat(),
        "detail":     json.dumps(decision),
    })

    # Aurora Serverless — full audit trail, reporting, BI queries
    rds_data.execute_statement(
        resourceArn=RDS_ARN,
        secretArn=RDS_SECRET_ARN,
        database=RDS_DATABASE,
        sql="""
            INSERT INTO claim_decisions
              (claim_id, status, net_payout, rejection_reasons, decided_at)
            VALUES
              (:claim_id, :status, :net_payout, :reasons, NOW())
            ON CONFLICT (claim_id) DO UPDATE
              SET status=EXCLUDED.status, net_payout=EXCLUDED.net_payout,
                  decided_at=EXCLUDED.decided_at
        """,
        parameters=[
            {"name": "claim_id",   "value": {"stringValue": claim_id}},
            {"name": "status",     "value": {"stringValue": decision["status"]}},
            {"name": "net_payout", "value": {"stringValue": decision["payout"]["net_payout"]}},
            {"name": "reasons",    "value": {"stringValue": json.dumps(decision.get("rejection_reasons", []))}},
        ],
    )


# ── Lambda handler ────────────────────────────────────────────────────────────

def lambda_handler(event, context):
    # SNS delivers the claim JSON published by claim_processor.py
    message = json.loads(event["Records"][0]["Sns"]["Message"])

    claim_id = message["claim_id"]
    fields   = message["fields"]
    tables   = message["tables"]

    # Skip if still pending human review — A2I will re-trigger after correction
    if message.get("status") == "PENDING_REVIEW":
        print(f"Claim {claim_id} awaiting human review — skipping adjudication")
        return {"statusCode": 202, "body": "PENDING_REVIEW"}

    policy_number = fields.get("Policy Number", {}).get("value", "")
    hospital_name = fields.get("Hospital Name", {}).get("value", "")

    policy = get_policy(policy_number)

    rejection_reasons = validate(fields, policy)

    if not check_provider(hospital_name):
        rejection_reasons.append("PROVIDER_NOT_IN_NETWORK")

    if rejection_reasons:
        decision = {"status": "REJECTED", "rejection_reasons": rejection_reasons,
                    "payout": {"net_payout": "0"}}
    else:
        payout   = calculate_payout(tables, policy)
        decision = {"status": "APPROVED", "payout": payout}

    persist_decision(claim_id, decision)
    print(f"Claim {claim_id} → {decision['status']} | payout: {decision['payout']['net_payout']}")

    return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, **decision})}
