"""
Isolation Forest — Billing Anomaly Detection
Runs as a SageMaker Processing Job every 15 minutes.
Detects claims where procedure costs are statistically anomalous
compared to historical billing for the same procedure codes.

Input:  s3://claims-bucket/billing/recent_claims.csv
Output: s3://claims-bucket/billing/anomalies.csv  (flagged claim_ids + anomaly scores)
"""

import os
import pandas as pd
from sklearn.ensemble import IsolationForest
from sklearn.preprocessing import StandardScaler
import boto3

s3 = boto3.client("s3")

INPUT_PATH  = "/opt/ml/processing/input/recent_claims.csv"
OUTPUT_PATH = "/opt/ml/processing/output/anomalies.csv"

# Features that represent billing behaviour per claim
BILLING_FEATURES = [
    "procedure_count",       # number of line items
    "total_billed",          # total claim amount
    "avg_cost_per_procedure",# total / procedure_count
    "max_single_procedure",  # most expensive single line item
    "days_since_admission",  # length of stay proxy
]


def load_data() -> pd.DataFrame:
    df = pd.read_csv(INPUT_PATH)
    df["avg_cost_per_procedure"] = df["total_billed"] / df["procedure_count"].replace(0, 1)
    return df


def detect_anomalies(df: pd.DataFrame) -> pd.DataFrame:
    X = df[BILLING_FEATURES].fillna(0)
    X_scaled = StandardScaler().fit_transform(X)

    model = IsolationForest(
        n_estimators=200,
        contamination=0.05,   # expect ~5% anomalous claims
        random_state=42,
    )
    df["anomaly_score"] = model.fit_predict(X_scaled)          # -1 = anomaly, 1 = normal
    df["anomaly_raw"]   = model.decision_function(X_scaled)    # continuous score

    return df[df["anomaly_score"] == -1][["claim_id", "anomaly_raw"]].sort_values("anomaly_raw")


if __name__ == "__main__":
    df        = load_data()
    anomalies = detect_anomalies(df)

    os.makedirs(os.path.dirname(OUTPUT_PATH), exist_ok=True)
    anomalies.to_csv(OUTPUT_PATH, index=False)

    print(f"Anomalies detected: {len(anomalies)} / {len(df)} claims")
    print(anomalies.head(10).to_string())
