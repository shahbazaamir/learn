"""
Medical Image Labelling Pipeline — SageMaker Ground Truth
Use case: Label chest X-ray images for pneumonia detection AI model.

Workforce strategy:
  - Mechanical Turk:  pre-screen images (is this a valid X-ray? / image quality check)
  - Private workforce: radiologists label valid images (Normal / Pneumonia / Uncertain)
  - Augmented AI (A2I): review low-confidence model predictions in production

Pipeline:
  1. Upload raw X-ray images to S3
  2. Pre-screen via Mechanical Turk Ground Truth job (quality filter)
  3. Label valid images via Private Workforce Ground Truth job
  4. Train SageMaker image classifier on labelled data
  5. Deploy endpoint — A2I reviews predictions below confidence threshold
"""

import json
import boto3
import sagemaker
from sagemaker.session import Session

session = Session()
role    = sagemaker.get_execution_role()
bucket  = session.default_bucket()
region  = "us-east-1"

sm     = boto3.client("sagemaker")
s3     = boto3.client("s3")

RAW_IMAGES_S3      = f"s3://{bucket}/xray/raw/"
SCREENED_S3        = f"s3://{bucket}/xray/screened/"
LABELS_S3          = f"s3://{bucket}/xray/labels/"
MANIFEST_S3        = f"s3://{bucket}/xray/manifests/"

PRIVATE_WORKFORCE_ARN = "arn:aws:sagemaker:us-east-1:<account-id>:workteam/private-crowd/radiologists"
A2I_FLOW_ARN          = "arn:aws:sagemaker:us-east-1:<account-id>:flow-definition/xray-review"
ENDPOINT_NAME         = "pneumonia-detector"
CONFIDENCE_THRESHOLD  = 0.85


# ── 1. Generate input manifest for Ground Truth ───────────────────────────────
# Manifest: one JSON line per image pointing to S3 URI

def create_manifest(image_keys: list[str], output_s3: str) -> str:
    lines = [json.dumps({"source-ref": f"s3://{bucket}/xray/raw/{k}"}) for k in image_keys]
    local = "/tmp/input.manifest"
    with open(local, "w") as f:
        f.write("\n".join(lines))
    key = "xray/manifests/input.manifest"
    s3.upload_file(local, bucket, key)
    return f"s3://{bucket}/{key}"


# ── 2. Step 1 — Mechanical Turk pre-screening job ────────────────────────────
# Workers answer: "Is this a valid chest X-ray?" Yes / No
# Filters out corrupt files, non-X-ray images, poor quality scans

def create_mturk_screening_job(manifest_s3: str) -> str:
    response = sm.create_labeling_job(
        LabelingJobName="xray-quality-screening",
        LabelAttributeName="valid-xray",
        InputConfig={
            "DataSource": {"S3DataSource": {"ManifestS3Uri": manifest_s3}},
            "DataAttributes": {"ContentClassifiers": ["FreeOfPersonallyIdentifiableInformation"]},
        },
        OutputConfig={"S3OutputPath": SCREENED_S3},
        RoleArn=role,
        LabelCategoryConfigS3Uri=_upload_label_config(
            "xray/manifests/screening_labels.json",
            {"document-version": "2018-11-28",
             "labels": [{"label": "valid"}, {"label": "invalid"}]},
        ),
        HumanTaskConfig={
            "WorkteamArn": "arn:aws:sagemaker:us-east-1:394669845002:workteam/public-crowd/default",
            "UiConfig": {"UiTemplateS3Uri": _upload_ui_template("screening")},
            "PreHumanTaskLambdaArn": f"arn:aws:lambda:{region}:432418664414:function:PRE-ImageMultiClass",
            "TaskTitle": "X-Ray Quality Check",
            "TaskDescription": "Is this image a valid chest X-ray suitable for medical analysis?",
            "NumberOfHumanWorkersPerDataObject": 3,
            "TaskTimeLimitInSeconds": 30,
            "AnnotationConsolidationConfig": {
                "AnnotationConsolidationLambdaArn": f"arn:aws:lambda:{region}:432418664414:function:ACS-ImageMultiClass",
            },
        },
    )
    print(f"MTurk screening job created: {response['LabelingJobArn']}")
    return response["LabelingJobArn"]


# ── 3. Step 2 — Private workforce radiologist labelling job ──────────────────
# Radiologists label each valid X-ray: Normal / Pneumonia / Uncertain

def create_radiologist_labelling_job(screened_manifest_s3: str) -> str:
    response = sm.create_labeling_job(
        LabelingJobName="xray-pneumonia-labelling",
        LabelAttributeName="pneumonia-label",
        InputConfig={
            "DataSource": {"S3DataSource": {"ManifestS3Uri": screened_manifest_s3}},
        },
        OutputConfig={"S3OutputPath": LABELS_S3},
        RoleArn=role,
        LabelCategoryConfigS3Uri=_upload_label_config(
            "xray/manifests/pneumonia_labels.json",
            {"document-version": "2018-11-28",
             "labels": [
                 {"label": "Normal"},
                 {"label": "Pneumonia"},
                 {"label": "Uncertain"},
             ]},
        ),
        HumanTaskConfig={
            "WorkteamArn": PRIVATE_WORKFORCE_ARN,
            "UiConfig": {"UiTemplateS3Uri": _upload_ui_template("labelling")},
            "PreHumanTaskLambdaArn": f"arn:aws:lambda:{region}:432418664414:function:PRE-ImageMultiClass",
            "TaskTitle": "Chest X-Ray Classification",
            "TaskDescription": "Classify the chest X-ray as Normal, Pneumonia, or Uncertain.",
            "NumberOfHumanWorkersPerDataObject": 2,   # 2 radiologists per image
            "TaskTimeLimitInSeconds": 300,
            "TaskAvailabilityLifetimeInSeconds": 86400,
            "AnnotationConsolidationConfig": {
                "AnnotationConsolidationLambdaArn": f"arn:aws:lambda:{region}:432418664414:function:ACS-ImageMultiClass",
            },
        },
    )
    print(f"Radiologist labelling job created: {response['LabelingJobArn']}")
    return response["LabelingJobArn"]


# ── 4. Train image classifier on labelled data ───────────────────────────────

def train_classifier(output_manifest_s3: str):
    image_uri = sagemaker.image_uris.retrieve("image-classification", region)
    estimator = sagemaker.estimator.Estimator(
        image_uri=image_uri,
        role=role,
        instance_count=1,
        instance_type="ml.p3.2xlarge",
        output_path=f"s3://{bucket}/xray/model/",
        hyperparameters={
            "num_classes":        3,
            "num_training_samples": 5000,
            "epochs":             10,
            "learning_rate":      0.001,
            "mini_batch_size":    32,
            "use_pretrained_model": 1,   # transfer learning from ImageNet
        },
    )
    estimator.fit(
        {"train": sagemaker.inputs.TrainingInput(output_manifest_s3, content_type="application/x-recordio")},
        job_name="pneumonia-classifier-v1",
    )
    estimator.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.xlarge",
        endpoint_name=ENDPOINT_NAME,
    )
    print(f"Endpoint deployed: {ENDPOINT_NAME}")


# ── 5. A2I — review low-confidence predictions in production ─────────────────

def invoke_with_a2i_review(image_s3_uri: str, claim_id: str):
    """
    Called at inference time. If model confidence < threshold,
    route to A2I for radiologist review before returning result.
    """
    sagemaker_rt = boto3.client("sagemaker-runtime")
    a2i          = boto3.client("sagemaker-a2i-runtime")

    # Get model prediction
    resp       = sagemaker_rt.invoke_endpoint(
        EndpointName=ENDPOINT_NAME,
        ContentType="application/x-image",
        Body=_download_image(image_s3_uri),
    )
    prediction = json.loads(resp["Body"].read())
    top_score  = max(prediction["prediction"])
    top_label  = ["Normal", "Pneumonia", "Uncertain"][prediction["prediction"].index(top_score)]

    if top_score < CONFIDENCE_THRESHOLD:
        # Route to A2I human review
        a2i.start_human_loop(
            HumanLoopName=f"xray-review-{claim_id}",
            FlowDefinitionArn=A2I_FLOW_ARN,
            HumanLoopInput={
                "InputContent": json.dumps({
                    "image_s3_uri": image_s3_uri,
                    "model_label":  top_label,
                    "confidence":   top_score,
                })
            },
        )
        return {"label": "PENDING_REVIEW", "confidence": top_score}

    return {"label": top_label, "confidence": top_score}


# ── Helpers ───────────────────────────────────────────────────────────────────

def _upload_label_config(key: str, config: dict) -> str:
    import tempfile, json
    with tempfile.NamedTemporaryFile("w", suffix=".json", delete=False) as f:
        json.dump(config, f)
        f.flush()
        s3.upload_file(f.name, bucket, key)
    return f"s3://{bucket}/{key}"


def _upload_ui_template(job_type: str) -> str:
    # Minimal Liquid template — Ground Truth renders the image + label buttons
    template = """
    <crowd-form>
      <crowd-image-classifier
        src="{{ task.input.taskObject | grant_read_access }}"
        categories="{{ task.input.labels | to_json | escape }}"
        header="Classify this chest X-ray">
      </crowd-image-classifier>
    </crowd-form>
    """
    key = f"xray/ui/{job_type}_template.html"
    s3.put_object(Bucket=bucket, Key=key, Body=template.encode())
    return f"s3://{bucket}/{key}"


def _download_image(s3_uri: str) -> bytes:
    b, k = s3_uri.replace("s3://", "").split("/", 1)
    return s3.get_object(Bucket=b, Key=k)["Body"].read()


# ── Orchestrate full pipeline (run once to bootstrap) ────────────────────────

if __name__ == "__main__":
    # List raw image keys (in production, discover from S3 listing)
    image_keys = [f"batch_001/img_{i:04d}.jpg" for i in range(1, 101)]

    print("Creating input manifest...")
    manifest = create_manifest(image_keys, MANIFEST_S3)

    print("Step 1: Launching MTurk quality screening job...")
    create_mturk_screening_job(manifest)
    # After job completes, screened output manifest is at:
    screened_manifest = f"{SCREENED_S3}xray-quality-screening/manifests/output/output.manifest"

    print("Step 2: Launching radiologist labelling job...")
    create_radiologist_labelling_job(screened_manifest)
    # After job completes, labelled output manifest is at:
    labelled_manifest = f"{LABELS_S3}xray-pneumonia-labelling/manifests/output/output.manifest"

    print("Step 3: Training image classifier...")
    train_classifier(labelled_manifest)

    print("Pipeline complete. A2I reviews active on endpoint:", ENDPOINT_NAME)
