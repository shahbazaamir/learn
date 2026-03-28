"""
Analyst Fraud Label Updater
Called when a fraud analyst reviews a held claim and marks it FRAUD or LEGIT.
Updates the SageMaker Feature Store record so the weekly retraining job
picks up the correct label.

Triggered by: analyst portal API Gateway → Lambda
"""

import json
import os
import boto3
from datetime import datetime, timezone

featurestore = boto3.client("sagemaker-featurestore-runtime")
sqs          = boto3.client("sqs")

FEATURE_GROUP   = os.environ["FEATURE_GROUP"]
FRAUD_QUEUE_URL = os.environ["FRAUD_QUEUE_URL"]

VALID_LABELS = {"FRAUD", "LEGIT"}


def lambda_handler(event, context):
    body      = json.loads(event.get("body", "{}"))
    claim_id  = body.get("claim_id")
    label     = body.get("label", "").upper()
    analyst   = body.get("analyst_id", "unknown")

    if not claim_id or label not in VALID_LABELS:
        return {
            "statusCode": 400,
            "body": json.dumps({"error": f"claim_id and label ({VALID_LABELS}) are required"}),
        }

    # Update Feature Store record with confirmed label
    featurestore.put_record(
        FeatureGroupName=FEATURE_GROUP,
        Record=[
            {"FeatureName": "claim_id",    "ValueAsString": claim_id},
            {"FeatureName": "label",       "ValueAsString": label},
            {"FeatureName": "labelled_by", "ValueAsString": analyst},
            {"FeatureName": "labelled_at", "ValueAsString": datetime.now(timezone.utc).isoformat()},
        ],
    )

    # Remove from fraud queue — analyst has made a decision
    sqs.send_message(
        QueueUrl=FRAUD_QUEUE_URL,
        MessageBody=json.dumps({
            "claim_id": claim_id,
            "action":   "RESOLVED",
            "label":    label,
            "analyst":  analyst,
        }),
    )

    print(f"Claim {claim_id} labelled {label} by {analyst}")
    return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, "label": label})}
