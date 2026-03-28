"""
First-Time Training Bootstrap — Fraud XGBoost Classifier
Run once to train the initial model on historical labelled claims
and create the SageMaker endpoint that claim_fraud_scorer.py calls.

Prerequisites:
  - Historical labelled claims CSV in S3 (manually labelled or from legacy system)
  - CSV format: label (0/1), net_payout, procedure_count, xgboost_score, duplicate_score, neptune_score
"""

import os
import boto3
import sagemaker
from sagemaker.estimator import Estimator
from sagemaker.inputs import TrainingInput
from sagemaker.session import Session
from sklearn.model_selection import train_test_split
import pandas as pd

session = Session()
role    = sagemaker.get_execution_role()
bucket  = session.default_bucket()
region  = boto3.Session().region_name

HISTORICAL_DATA_LOCAL = "data/historical_claims_labelled.csv"   # local CSV with known fraud labels
S3_PREFIX             = "fraud-bootstrap"
ENDPOINT_NAME         = os.environ.get("XGBOOST_ENDPOINT", "fraud-xgboost-endpoint")


# ── 1. Upload historical train/validation splits to S3 ───────────────────────

def upload_data() -> tuple[str, str]:
    df = pd.read_csv(HISTORICAL_DATA_LOCAL)

    feature_cols = ["net_payout", "procedure_count", "xgboost_score", "duplicate_score", "neptune_score"]
    df = df[["label"] + feature_cols].dropna()

    train_df, val_df = train_test_split(
        df, test_size=0.2, stratify=df["label"], random_state=42
    )

    train_df.to_csv("/tmp/train.csv", index=False, header=False)
    val_df.to_csv("/tmp/validation.csv",   index=False, header=False)

    s3 = boto3.client("s3")
    s3.upload_file("/tmp/train.csv",      bucket, f"{S3_PREFIX}/train/train.csv")
    s3.upload_file("/tmp/validation.csv", bucket, f"{S3_PREFIX}/validation/validation.csv")

    print(f"Uploaded {len(train_df)} train / {len(val_df)} validation records")
    return (
        f"s3://{bucket}/{S3_PREFIX}/train/",
        f"s3://{bucket}/{S3_PREFIX}/validation/",
    )


# ── 2. Train initial XGBoost model ───────────────────────────────────────────

def train(train_s3: str, val_s3: str) -> Estimator:
    xgb_image = sagemaker.image_uris.retrieve("xgboost", region, "1.7-1")
    estimator = Estimator(
        image_uri=xgb_image,
        instance_type="ml.m5.xlarge",
        instance_count=1,
        output_path=f"s3://{bucket}/{S3_PREFIX}/model/",
        role=role,
        hyperparameters={
            "objective":             "binary:logistic",
            "eval_metric":           "auc",
            "max_depth":             5,
            "eta":                   0.2,
            "num_round":             100,
            "early_stopping_rounds": 10,
            "scale_pos_weight":      19,   # ~5% fraud rate → 95/5 = 19
        },
    )
    estimator.fit(
        {
            "train":      TrainingInput(train_s3, content_type="text/csv"),
            "validation": TrainingInput(val_s3,   content_type="text/csv"),
        },
        job_name="fraud-xgboost-bootstrap",
    )
    return estimator


# ── 3. Deploy endpoint (used by claim_fraud_scorer.py) ───────────────────────

def deploy(estimator: Estimator):
    estimator.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.large",
        endpoint_name=ENDPOINT_NAME,
    )
    print(f"Endpoint created: {ENDPOINT_NAME}")
    print(f"Set env var XGBOOST_ENDPOINT={ENDPOINT_NAME} in claim_fraud_scorer Lambda")


# ── Run once ──────────────────────────────────────────────────────────────────

if __name__ == "__main__":
    print("Step 1: Uploading historical data...")
    train_s3, val_s3 = upload_data()

    print("Step 2: Training initial XGBoost model...")
    estimator = train(train_s3, val_s3)

    print("Step 3: Deploying endpoint...")
    deploy(estimator)
