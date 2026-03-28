"""
Billing Anomaly Detection — SageMaker Random Cut Forest (RCF)
Uses SageMaker's native unsupervised anomaly detection algorithm.

Pipeline:
  1. Load recent claims from S3, engineer billing features
  2. Train RCF model via SageMaker Training Job
  3. Run batch transform to score all claims
  4. Flag claims with anomaly score above threshold → S3 output
"""

import boto3
import numpy as np
import pandas as pd
import sagemaker
from sagemaker import RandomCutForest
from sagemaker.session import Session

session  = Session()
role     = sagemaker.get_execution_role()
bucket   = session.default_bucket()
prefix   = "claims/billing-anomaly"

RECENT_CLAIMS_S3 = f"s3://{bucket}/billing/recent_claims.csv"
TRAIN_DATA_S3    = f"s3://{bucket}/{prefix}/train/"
SCORE_INPUT_S3   = f"s3://{bucket}/{prefix}/score-input/"
SCORE_OUTPUT_S3  = f"s3://{bucket}/{prefix}/score-output/"

ANOMALY_THRESHOLD = 3.0   # RCF scores > 3 std deviations above mean = anomaly

BILLING_FEATURES = [
    "procedure_count",
    "total_billed",
    "avg_cost_per_procedure",
    "max_single_procedure",
    "days_since_admission",
]


# ── 1. Prepare feature matrix from recent claims CSV ─────────────────────────

def prepare_features(s3_uri: str) -> tuple[np.ndarray, pd.DataFrame]:
    local = "/tmp/recent_claims.csv"
    bucket_name, key = s3_uri.replace("s3://", "").split("/", 1)
    boto3.client("s3").download_file(bucket_name, key, local)

    df = pd.read_csv(local)
    df["avg_cost_per_procedure"] = df["total_billed"] / df["procedure_count"].replace(0, 1)
    X = df[BILLING_FEATURES].fillna(0).values.astype("float32")
    return X, df


# ── 2. Upload training data in RecordIO-protobuf format ──────────────────────

def upload_train_data(X: np.ndarray) -> str:
    buf = sagemaker.amazon.common.write_numpy_to_dense_tensor(
        io_utils=__import__("io").BytesIO(), array=X
    )
    buf.seek(0)
    key = f"{prefix}/train/train.data"
    boto3.client("s3").upload_fileobj(buf, bucket, key)
    return f"s3://{bucket}/{key}"


# ── 3. Train RCF model ────────────────────────────────────────────────────────

def train_rcf(train_s3_uri: str) -> RandomCutForest:
    rcf = RandomCutForest(
        role=role,
        instance_count=1,
        instance_type="ml.m4.xlarge",
        num_samples_per_tree=512,
        num_trees=50,
        eval_metrics=["accuracy", "precision_recall_fscore"],
    )
    rcf.fit(rcf.record_set(
        np.load("/tmp/X.npy"),   # reuse in-memory array
        channel="train",
    ))
    return rcf


# ── 4. Batch transform — score all claims ────────────────────────────────────

def score_claims(rcf: RandomCutForest, X: np.ndarray, df: pd.DataFrame) -> pd.DataFrame:
    # Use in-memory predictor for small batches; batch transform for large
    predictor = rcf.deploy(
        initial_instance_count=1,
        instance_type="ml.m4.xlarge",
    )

    response  = predictor.predict(X)
    scores    = np.array([r["score"] for r in response["scores"]])

    # Flag anomalies: score > mean + 3*std
    threshold = scores.mean() + ANOMALY_THRESHOLD * scores.std()
    df["anomaly_score"] = scores
    df["is_anomaly"]    = scores > threshold

    predictor.delete_endpoint()   # clean up — no persistent endpoint needed
    return df[df["is_anomaly"]][["claim_id", "anomaly_score"]].sort_values(
        "anomaly_score", ascending=False
    )


# ── 5. Write flagged claims back to S3 ───────────────────────────────────────

def save_anomalies(anomalies: pd.DataFrame):
    local = "/tmp/anomalies.csv"
    anomalies.to_csv(local, index=False)
    boto3.client("s3").upload_file(local, bucket, f"{prefix}/anomalies/anomalies.csv")
    print(f"Saved {len(anomalies)} anomalies → s3://{bucket}/{prefix}/anomalies/anomalies.csv")


# ── Orchestrate ───────────────────────────────────────────────────────────────

if __name__ == "__main__":
    print("Loading and preparing features...")
    X, df = prepare_features(RECENT_CLAIMS_S3)
    np.save("/tmp/X.npy", X)

    print("Training Random Cut Forest...")
    rcf = train_rcf(upload_train_data(X))

    print("Scoring claims...")
    anomalies = score_claims(rcf, X, df)

    print(f"Anomalies detected: {len(anomalies)} / {len(df)}")
    print(anomalies.head(10).to_string())

    save_anomalies(anomalies)
