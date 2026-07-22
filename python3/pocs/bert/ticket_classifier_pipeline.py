"""
Customer Support Ticket Classifier — Fine-tuned BERT
Production use case: Classify incoming support tickets by category + urgency,
auto-route to correct team queue via SQS.

BERT type: Fine-tuning (not feature extraction)
  - All BERT layers updated with small LR (2e-5)
  - Two classification heads on top of [CLS] token:
      1. Category:  Billing / Technical / Shipping / Returns
      2. Urgency:   High / Medium / Low

Pipeline:
  1. Fine-tune BERT on labelled ticket dataset via SageMaker Training Job
  2. Deploy multi-output endpoint
  3. Lambda: classify incoming ticket → route to SQS queue → notify agent via SNS
"""

import os
import json
import boto3
import sagemaker
from sagemaker.huggingface import HuggingFace, HuggingFaceModel
from sagemaker.session import Session

session = Session()
role    = sagemaker.get_execution_role()
bucket  = session.default_bucket()
region  = boto3.Session().region_name

ENDPOINT_NAME = "support-ticket-classifier"
PREFIX        = "bert-ticket-classifier"

# SQS queues per category (pre-created)
QUEUE_URLS = {
    "Billing":   os.environ.get("BILLING_QUEUE_URL"),
    "Technical": os.environ.get("TECHNICAL_QUEUE_URL"),
    "Shipping":  os.environ.get("SHIPPING_QUEUE_URL"),
    "Returns":   os.environ.get("RETURNS_QUEUE_URL"),
}
SNS_TOPIC_ARN = os.environ.get("AGENT_NOTIFY_TOPIC")


# ── 1. Upload labelled ticket dataset to S3 ───────────────────────────────────

def upload_dataset(train_csv: str, val_csv: str) -> tuple[str, str]:
    s3 = boto3.client("s3")
    for local, key in [
        (train_csv, f"{PREFIX}/data/train/train.csv"),
        (val_csv,   f"{PREFIX}/data/val/val.csv"),
    ]:
        s3.upload_file(local, bucket, key)
    return (
        f"s3://{bucket}/{PREFIX}/data/train/",
        f"s3://{bucket}/{PREFIX}/data/val/",
    )


# ── 2. Fine-tune BERT via SageMaker HuggingFace estimator ────────────────────

def train(train_s3: str, val_s3: str) -> HuggingFace:
    estimator = HuggingFace(
        entry_point="train.py",
        source_dir="scripts",
        role=role,
        instance_type="ml.p3.2xlarge",
        instance_count=1,
        transformers_version="4.26",
        pytorch_version="1.13",
        py_version="py39",
        output_path=f"s3://{bucket}/{PREFIX}/model/",
        hyperparameters={
            "model_name":        "bert-base-uncased",
            "epochs":            4,
            "learning_rate":     2e-5,
            "train_batch_size":  32,
            "eval_batch_size":   64,
            "max_seq_length":    128,
            "num_categories":    4,
            "num_urgencies":     3,
            "warmup_steps":      100,
            "weight_decay":      0.01,
        },
    )
    estimator.fit(
        {"train": train_s3, "val": val_s3},
        job_name="bert-ticket-classifier-finetune",
        wait=True,
    )
    return estimator


# ── 3. Deploy endpoint ────────────────────────────────────────────────────────

def deploy(estimator: HuggingFace):
    model = HuggingFaceModel(
        model_data=estimator.model_data,
        role=role,
        transformers_version="4.26",
        pytorch_version="1.13",
        py_version="py39",
        entry_point="inference.py",
        source_dir="scripts",
    )
    model.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.xlarge",
        endpoint_name=ENDPOINT_NAME,
    )
    print(f"Endpoint deployed: {ENDPOINT_NAME}")


# ── 4. Lambda — classify ticket + route to SQS + notify agent ────────────────

def lambda_handler(event, context):
    """
    Triggered by API Gateway when a new support ticket is submitted.
    Classifies ticket, routes to correct SQS queue, notifies on-call agent if High urgency.
    """
    sm_rt    = boto3.client("sagemaker-runtime")
    sqs      = boto3.client("sqs")
    sns      = boto3.client("sns")
    dynamodb = boto3.resource("dynamodb")

    body      = json.loads(event["body"])
    ticket_id = body["ticket_id"]
    text      = body["text"]
    customer  = body.get("customer_id", "unknown")

    # Classify via BERT endpoint
    response   = sm_rt.invoke_endpoint(
        EndpointName=ENDPOINT_NAME,
        ContentType="application/json",
        Body=json.dumps({"text": text}),
    )
    prediction = json.loads(response["Body"].read())
    category   = prediction["category"]    # e.g. "Technical"
    urgency    = prediction["urgency"]     # e.g. "High"
    cat_conf   = prediction["category_confidence"]
    urg_conf   = prediction["urgency_confidence"]

    # Route to category SQS queue
    queue_url = QUEUE_URLS.get(category)
    if queue_url:
        sqs.send_message(
            QueueUrl=queue_url,
            MessageBody=json.dumps({
                "ticket_id":  ticket_id,
                "customer":   customer,
                "text":       text,
                "category":   category,
                "urgency":    urgency,
                "cat_conf":   cat_conf,
                "urg_conf":   urg_conf,
            }),
            MessageAttributes={
                "urgency": {"StringValue": urgency, "DataType": "String"},
            },
        )

    # Notify on-call agent immediately for High urgency
    if urgency == "High" and SNS_TOPIC_ARN:
        sns.publish(
            TopicArn=SNS_TOPIC_ARN,
            Subject=f"[HIGH] {category} ticket from {customer}",
            Message=f"Ticket {ticket_id}\n\n{text}\n\nCategory: {category} ({cat_conf:.0%})\nUrgency: High ({urg_conf:.0%})",
        )

    # Persist classification to DynamoDB
    dynamodb.Table(os.environ["TICKETS_TABLE"]).put_item(Item={
        "ticket_id":  ticket_id,
        "customer":   customer,
        "category":   category,
        "urgency":    urgency,
        "cat_conf":   str(round(cat_conf, 4)),
        "urg_conf":   str(round(urg_conf, 4)),
        "status":     "ROUTED",
    })

    print(f"Ticket {ticket_id} → {category} / {urgency} (conf: {cat_conf:.2%} / {urg_conf:.2%})")
    return {
        "statusCode": 200,
        "body": json.dumps({"ticket_id": ticket_id, "category": category, "urgency": urgency}),
    }


if __name__ == "__main__":
    print("Uploading dataset...")
    train_s3, val_s3 = upload_dataset("data/train.csv", "data/val.csv")

    print("Fine-tuning BERT...")
    estimator = train(train_s3, val_s3)

    print("Deploying endpoint...")
    deploy(estimator)
