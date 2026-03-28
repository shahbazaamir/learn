"""
Exports labelled records from SageMaker Feature Store offline store.
Filters only confirmed labels (FRAUD or LEGIT), splits into train/validation.
Called by ExportFeatureStore ProcessingStep in fraud_retraining_pipeline.py
"""

import argparse
import os
import boto3
import pandas as pd
from sklearn.model_selection import train_test_split

parser = argparse.ArgumentParser()
parser.add_argument("--feature-group", required=True)
parser.add_argument("--label-filter",  default="FRAUD,LEGIT")
args = parser.parse_args()

sm     = boto3.client("sagemaker")
athena = boto3.client("athena")
s3     = boto3.client("s3")

ATHENA_OUTPUT = os.environ.get("ATHENA_OUTPUT", "s3://sagemaker-query-results/")


def get_offline_store_table(feature_group: str) -> tuple[str, str]:
    fg = sm.describe_feature_group(FeatureGroupName=feature_group)
    config = fg["OfflineStoreConfig"]["DataCatalogConfig"]
    return config["Database"], config["TableName"]


def query_labelled_records(database: str, table: str, labels: list[str]) -> pd.DataFrame:
    label_list = ", ".join(f"'{l}'" for l in labels)
    query = f"""
        SELECT * FROM "{database}"."{table}"
        WHERE label IN ({label_list})
        AND write_time = (
            SELECT MAX(write_time) FROM "{database}"."{table}" t2
            WHERE t2.claim_id = "{table}".claim_id
        )
    """
    resp = athena.start_query_execution(
        QueryString=query,
        ResultConfiguration={"OutputLocation": ATHENA_OUTPUT},
    )
    qid = resp["QueryExecutionId"]

    import time
    while True:
        status = athena.get_query_execution(QueryExecutionId=qid)["QueryExecution"]["Status"]["State"]
        if status == "SUCCEEDED":
            break
        if status in ("FAILED", "CANCELLED"):
            raise RuntimeError(f"Athena query {status}")
        time.sleep(3)

    result = athena.get_query_results(QueryExecutionId=qid)
    cols   = [c["Label"] for c in result["ResultSet"]["ResultSetMetadata"]["ColumnInfo"]]
    rows   = [[f.get("VarCharValue", "") for f in r["Data"]] for r in result["ResultSet"]["Rows"][1:]]
    return pd.DataFrame(rows, columns=cols)


if __name__ == "__main__":
    labels   = args.label_filter.split(",")
    database, table = get_offline_store_table(args.feature_group)
    df = query_labelled_records(database, table, labels)

    # Encode label as binary: FRAUD=1, LEGIT=0
    df["label"] = (df["label"] == "FRAUD").astype(int)

    feature_cols = ["net_payout", "procedure_count", "xgboost_score", "duplicate_score", "neptune_score"]
    df = df[["label"] + feature_cols].dropna()

    train_df, val_df = train_test_split(df, test_size=0.2, stratify=df["label"], random_state=42)

    os.makedirs("/opt/ml/processing/train",      exist_ok=True)
    os.makedirs("/opt/ml/processing/validation", exist_ok=True)

    train_df.to_csv("/opt/ml/processing/train/train.csv",           index=False, header=False)
    val_df.to_csv("/opt/ml/processing/validation/validation.csv",   index=False, header=False)

    print(f"Exported {len(train_df)} train / {len(val_df)} validation records")
