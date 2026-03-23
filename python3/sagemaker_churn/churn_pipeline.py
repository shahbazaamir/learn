"""
Customer Churn Prediction Pipeline — SageMaker Data Wrangler
Use case: Telecom company predicts which customers will cancel their subscription.

Pipeline:
  1. Export Data Wrangler .flow transformations as a Processing Job
  2. Train XGBoost model on transformed data
  3. Deploy model endpoint
  4. Run batch inference on new customers
"""

import boto3
import sagemaker
from sagemaker import Session
from sagemaker.processing import ProcessingInput, ProcessingOutput
from sagemaker.xgboost import XGBoost
from sagemaker.inputs import TrainingInput
from sagemaker.transformer import Transformer

session    = Session()
role       = sagemaker.get_execution_role()
bucket     = session.default_bucket()
region     = boto3.Session().region_name
prefix     = "churn"

RAW_DATA_S3      = f"s3://{bucket}/{prefix}/raw/customer_data.csv"
FLOW_S3_URI      = f"s3://{bucket}/{prefix}/flow/churn.flow"          # exported from Data Wrangler UI
PROCESSED_S3     = f"s3://{bucket}/{prefix}/processed/"
TRAIN_S3         = f"s3://{bucket}/{prefix}/train/"
MODEL_S3         = f"s3://{bucket}/{prefix}/model/"


# ── 1. Run Data Wrangler flow as a SageMaker Processing Job ──────────────────
# The .flow file encodes all transformations built in the Data Wrangler UI:
#   - Drop nulls in tenure, monthly_charges
#   - Encode categorical: contract_type, payment_method
#   - Feature engineering: avg_charge_per_month, support_calls_ratio
#   - Train/test split (80/20)

def run_data_wrangler_job():
    sm = boto3.client("sagemaker")

    sm.create_processing_job(
        ProcessingJobName="churn-data-wrangler-job",
        RoleArn=role,
        AppSpecification={
            "ImageUri": f"415577184552.dkr.ecr.{region}.amazonaws.com/sagemaker-data-wrangler-container:3.x",
            "ContainerEntrypoint": ["python3", "/opt/ml/processing/input/flow/churn.flow"],
        },
        ProcessingInputs=[
            {
                "InputName": "flow",
                "S3Input": {
                    "S3Uri": FLOW_S3_URI,
                    "LocalPath": "/opt/ml/processing/input/flow",
                    "S3DataType": "S3Prefix",
                    "S3InputMode": "File",
                },
            },
            {
                "InputName": "raw-data",
                "S3Input": {
                    "S3Uri": RAW_DATA_S3,
                    "LocalPath": "/opt/ml/processing/input/data",
                    "S3DataType": "S3Prefix",
                    "S3InputMode": "File",
                },
            },
        ],
        ProcessingOutputConfig={
            "Outputs": [
                {
                    "OutputName": "processed",
                    "S3Output": {
                        "S3Uri": PROCESSED_S3,
                        "LocalPath": "/opt/ml/processing/output",
                        "S3UploadMode": "EndOfJob",
                    },
                }
            ]
        },
        ProcessingResources={
            "ClusterConfig": {
                "InstanceCount": 1,
                "InstanceType": "ml.m5.4xlarge",
                "VolumeSizeInGB": 30,
            }
        },
    )
    print("Data Wrangler processing job started: churn-data-wrangler-job")


# ── 2. Train XGBoost churn model ─────────────────────────────────────────────

def train_model():
    xgb = XGBoost(
        entry_point="train.py",           # minimal script — see below
        framework_version="1.7-1",
        instance_type="ml.m5.xlarge",
        instance_count=1,
        output_path=MODEL_S3,
        role=role,
        hyperparameters={
            "max_depth": 5,
            "eta": 0.2,
            "objective": "binary:logistic",
            "num_round": 100,
            "eval_metric": "auc",
        },
    )
    xgb.fit({
        "train": TrainingInput(f"{PROCESSED_S3}train/", content_type="text/csv"),
        "validation": TrainingInput(f"{PROCESSED_S3}validation/", content_type="text/csv"),
    })
    return xgb


# ── 3. Deploy real-time endpoint ─────────────────────────────────────────────

def deploy_endpoint(estimator):
    predictor = estimator.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.large",
        endpoint_name="churn-predictor",
    )
    print("Endpoint deployed: churn-predictor")
    return predictor


# ── 4. Batch inference for monthly churn scoring ─────────────────────────────

def run_batch_inference(estimator):
    transformer = estimator.transformer(
        instance_count=1,
        instance_type="ml.m5.xlarge",
        output_path=f"s3://{bucket}/{prefix}/batch-output/",
        strategy="MultiRecord",
        assemble_with="Line",
    )
    transformer.transform(
        data=f"s3://{bucket}/{prefix}/batch-input/",
        content_type="text/csv",
        split_type="Line",
        job_name="churn-batch-scoring",
    )
    print("Batch transform job started: churn-batch-scoring")


# ── Orchestrate full pipeline ─────────────────────────────────────────────────

if __name__ == "__main__":
    print("Step 1: Running Data Wrangler transformation job...")
    run_data_wrangler_job()

    print("Step 2: Training XGBoost model...")
    estimator = train_model()

    print("Step 3: Deploying real-time endpoint...")
    deploy_endpoint(estimator)

    print("Step 4: Running monthly batch scoring...")
    run_batch_inference(estimator)
