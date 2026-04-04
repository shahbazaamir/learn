"""
Retail Product Defect Detection — Transfer Learning (ResNet-50)
Production use case: Factory line cameras capture product images every 500ms.
Model classifies: OK / SCRATCH / DENT / DISCOLORATION

Pipeline:
  1. Prepare & upload labelled images to S3 in RecordIO format
  2. Fine-tune SageMaker Image Classification (ResNet-50, ImageNet weights)
  3. Evaluate — only deploy if accuracy > threshold
  4. Deploy real-time endpoint with auto-scaling
  5. Invoke from factory line Lambda — trigger rejection arm on defect
"""

import os
import json
import boto3
import sagemaker
from sagemaker.estimator import Estimator
from sagemaker.inputs import TrainingInput
from sagemaker.session import Session

session = Session()
role    = sagemaker.get_execution_role()
bucket  = session.default_bucket()
region  = boto3.Session().region_name

PREFIX          = "defect-detection"
ENDPOINT_NAME   = "product-defect-detector"
NUM_CLASSES     = 4          # OK, SCRATCH, DENT, DISCOLORATION
ACCURACY_GATE   = 0.92       # minimum validation accuracy to deploy
IMAGE_URI       = sagemaker.image_uris.retrieve("image-classification", region)


# ── 1. Upload dataset (RecordIO format expected by SageMaker IC) ──────────────

def upload_dataset(train_rec_local: str, val_rec_local: str) -> tuple[str, str]:
    """
    Expects pre-converted .rec files (use im2rec tool or scripts/convert_to_recordio.py).
    Returns S3 URIs for train and validation channels.
    """
    s3 = boto3.client("s3")
    for local, key in [
        (train_rec_local, f"{PREFIX}/train/train.rec"),
        (val_rec_local,   f"{PREFIX}/validation/val.rec"),
    ]:
        s3.upload_file(local, bucket, key)
        print(f"Uploaded {local} → s3://{bucket}/{key}")

    return (
        f"s3://{bucket}/{PREFIX}/train/",
        f"s3://{bucket}/{PREFIX}/validation/",
    )


# ── 2. Fine-tune ResNet-50 with transfer learning ────────────────────────────

def train(train_s3: str, val_s3: str) -> Estimator:
    estimator = Estimator(
        image_uri=IMAGE_URI,
        role=role,
        instance_count=1,
        instance_type="ml.p3.2xlarge",       # V100 GPU — fine-tuning needs GPU
        output_path=f"s3://{bucket}/{PREFIX}/model/",
        hyperparameters={
            "num_layers":             50,     # ResNet-50
            "use_pretrained_model":   1,      # transfer learning from ImageNet
            "num_classes":            NUM_CLASSES,
            "num_training_samples":   2000,   # ~500 images × 4 augmentations
            "epochs":                 20,
            "learning_rate":          0.001,
            "mini_batch_size":        32,
            "augmentation_type":      "crop_color_transform",
            "early_stopping":         True,
            "early_stopping_patience": 3,
            "image_shape":            "3,224,224",
            "top_k":                  2,
        },
    )
    estimator.fit(
        {
            "train":      TrainingInput(train_s3, content_type="application/x-recordio"),
            "validation": TrainingInput(val_s3,   content_type="application/x-recordio"),
        },
        job_name="defect-resnet50-finetune",
        wait=True,
    )
    return estimator


# ── 3. Evaluate — gate deployment on validation accuracy ─────────────────────

def get_best_accuracy(estimator: Estimator) -> float:
    sm     = boto3.client("sagemaker")
    job    = sm.describe_training_job(TrainingJobName=estimator.latest_training_job.name)
    # SageMaker Image Classification logs validation:accuracy to CloudWatch
    cw     = boto3.client("cloudwatch")
    resp   = cw.get_metric_statistics(
        Namespace="SageMaker",
        MetricName="validation:accuracy",
        Dimensions=[{"Name": "TrainingJobName", "Value": estimator.latest_training_job.name}],
        StartTime=job["TrainingStartTime"],
        EndTime=job["TrainingEndTime"],
        Period=3600,
        Statistics=["Maximum"],
    )
    if not resp["Datapoints"]:
        return 0.0
    return max(d["Maximum"] for d in resp["Datapoints"])


# ── 4. Deploy endpoint with auto-scaling ─────────────────────────────────────

def deploy(estimator: Estimator):
    predictor = estimator.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.xlarge",
        endpoint_name=ENDPOINT_NAME,
    )

    # Auto-scaling: scale out when invocations > 100/min per instance
    aas = boto3.client("application-autoscaling")
    aas.register_scalable_target(
        ServiceNamespace="sagemaker",
        ResourceId=f"endpoint/{ENDPOINT_NAME}/variant/AllTraffic",
        ScalableDimension="sagemaker:variant:DesiredInstanceCount",
        MinCapacity=1,
        MaxCapacity=4,
    )
    aas.put_scaling_policy(
        PolicyName="defect-detector-scaling",
        ServiceNamespace="sagemaker",
        ResourceId=f"endpoint/{ENDPOINT_NAME}/variant/AllTraffic",
        ScalableDimension="sagemaker:variant:DesiredInstanceCount",
        PolicyType="TargetTrackingScaling",
        TargetTrackingScalingPolicyConfiguration={
            "TargetValue": 100.0,
            "PredefinedMetricSpecification": {
                "PredefinedMetricType": "SageMakerVariantInvocationsPerInstance"
            },
            "ScaleInCooldown":  120,
            "ScaleOutCooldown": 60,
        },
    )
    print(f"Endpoint deployed with auto-scaling: {ENDPOINT_NAME}")
    return predictor


# ── 5. Factory line Lambda — invoke endpoint per camera frame ─────────────────

def lambda_handler(event, context):
    """
    Triggered by IoT Rule when factory camera uploads image to S3.
    Classifies image and triggers rejection arm via IoT if defective.
    """
    s3_client    = boto3.client("s3")
    sm_rt        = boto3.client("sagemaker-runtime")
    iot          = boto3.client("iot-data", endpoint_url=os.environ["IOT_ENDPOINT"])

    rec_bucket = event["Records"][0]["s3"]["bucket"]["name"]
    rec_key    = event["Records"][0]["s3"]["object"]["key"]
    product_id = rec_key.split("/")[-1].replace(".jpg", "")

    image_bytes = s3_client.get_object(Bucket=rec_bucket, Key=rec_key)["Body"].read()

    response = sm_rt.invoke_endpoint(
        EndpointName=ENDPOINT_NAME,
        ContentType="application/x-image",
        Body=image_bytes,
    )
    result     = json.loads(response["Body"].read())
    labels     = ["OK", "SCRATCH", "DENT", "DISCOLORATION"]
    scores     = result["prediction"]
    top_idx    = scores.index(max(scores))
    top_label  = labels[top_idx]
    confidence = scores[top_idx]

    print(f"Product {product_id}: {top_label} ({confidence:.2%})")

    if top_label != "OK" and confidence > 0.80:
        # Publish to IoT topic — factory PLC triggers physical rejection arm
        iot.publish(
            topic=f"factory/line1/reject",
            payload=json.dumps({
                "product_id": product_id,
                "defect":     top_label,
                "confidence": confidence,
                "image_key":  rec_key,
            }),
        )

    # Log to DynamoDB for traceability
    boto3.resource("dynamodb").Table(os.environ["INSPECTION_TABLE"]).put_item(Item={
        "product_id": product_id,
        "label":      top_label,
        "confidence": str(round(confidence, 4)),
        "image_key":  rec_key,
    })

    return {"statusCode": 200, "body": json.dumps({"product_id": product_id, "label": top_label})}


# ── Orchestrate bootstrap ─────────────────────────────────────────────────────

if __name__ == "__main__":
    print("Uploading dataset...")
    train_s3, val_s3 = upload_dataset(
        "data/train.rec",
        "data/val.rec",
    )

    print("Fine-tuning ResNet-50...")
    estimator = train(train_s3, val_s3)

    print("Evaluating model...")
    accuracy = get_best_accuracy(estimator)
    print(f"Validation accuracy: {accuracy:.4f}")

    if accuracy < ACCURACY_GATE:
        print(f"Accuracy {accuracy:.2%} below gate {ACCURACY_GATE:.2%} — not deploying")
    else:
        print("Deploying endpoint with auto-scaling...")
        deploy(estimator)
