"""
Weekly XGBoost Retraining Pipeline — SageMaker Pipelines
Reads labelled records from SageMaker Feature Store (written by analyst_label_updater.py),
retrains the XGBoost fraud classifier, and updates the endpoint used by claim_fraud_scorer.py
only if the new model AUC exceeds the current deployed model.

Triggered by: EventBridge cron — every Monday 02:00 UTC
"""

import os
import boto3
import sagemaker
from sagemaker.workflow.pipeline import Pipeline
from sagemaker.workflow.steps import ProcessingStep, TrainingStep
from sagemaker.workflow.condition_step import ConditionStep
from sagemaker.workflow.conditions import ConditionGreaterThan
from sagemaker.workflow.properties import PropertyFile
from sagemaker.workflow.parameters import ParameterFloat, ParameterString
from sagemaker.processing import ScriptProcessor, ProcessingInput, ProcessingOutput
from sagemaker.estimator import Estimator
from sagemaker.inputs import TrainingInput
from sagemaker.workflow.step_collections import RegisterModel

session    = sagemaker.Session()
role       = sagemaker.get_execution_role()
bucket     = session.default_bucket()
region     = boto3.Session().region_name

FEATURE_GROUP   = os.environ["FEATURE_GROUP"]
ENDPOINT_NAME   = os.environ["XGBOOST_ENDPOINT"]        # same endpoint claim_fraud_scorer.py calls
MODEL_PKG_GROUP = "fraud-xgboost-models"

# ── Pipeline parameters (can be overridden at execution time) ─────────────────
auc_threshold   = ParameterFloat(name="AucThreshold",   default_value=0.85)
instance_type   = ParameterString(name="InstanceType",  default_value="ml.m5.xlarge")


# ── Step 1: Export labelled records from Feature Store → S3 CSV ──────────────

def feature_export_step() -> ProcessingStep:
    processor = ScriptProcessor(
        image_uri=sagemaker.image_uris.retrieve("sklearn", region, "1.2-1"),
        command=["python3"],
        instance_type="ml.m5.large",
        instance_count=1,
        role=role,
    )
    return ProcessingStep(
        name="ExportFeatureStore",
        processor=processor,
        code="scripts/export_features.py",
        job_arguments=["--feature-group", FEATURE_GROUP, "--label-filter", "FRAUD,LEGIT"],
        outputs=[
            ProcessingOutput(output_name="train",      source="/opt/ml/processing/train"),
            ProcessingOutput(output_name="validation", source="/opt/ml/processing/validation"),
        ],
    )


# ── Step 2: Train XGBoost on exported labelled data ──────────────────────────

def training_step(export_step: ProcessingStep) -> TrainingStep:
    xgb_image = sagemaker.image_uris.retrieve("xgboost", region, "1.7-1")
    estimator = Estimator(
        image_uri=xgb_image,
        instance_type=instance_type,
        instance_count=1,
        output_path=f"s3://{bucket}/fraud-model/output",
        role=role,
        hyperparameters={
            "objective":   "binary:logistic",
            "eval_metric": "auc",
            "max_depth":   5,
            "eta":         0.2,
            "num_round":   100,
            "early_stopping_rounds": 10,
        },
    )
    return TrainingStep(
        name="TrainFraudXGBoost",
        estimator=estimator,
        inputs={
            "train": TrainingInput(
                export_step.properties.ProcessingOutputConfig.Outputs["train"].S3Output.S3Uri,
                content_type="text/csv",
            ),
            "validation": TrainingInput(
                export_step.properties.ProcessingOutputConfig.Outputs["validation"].S3Output.S3Uri,
                content_type="text/csv",
            ),
        },
    )


# ── Step 3: Evaluate new model AUC ───────────────────────────────────────────

def evaluation_step(train_step: TrainingStep) -> tuple[ProcessingStep, PropertyFile]:
    processor = ScriptProcessor(
        image_uri=sagemaker.image_uris.retrieve("sklearn", region, "1.2-1"),
        command=["python3"],
        instance_type="ml.m5.large",
        instance_count=1,
        role=role,
    )
    eval_report = PropertyFile(name="EvalReport", output_name="evaluation", path="evaluation.json")
    step = ProcessingStep(
        name="EvaluateModel",
        processor=processor,
        code="scripts/evaluate.py",
        inputs=[
            ProcessingInput(
                source=train_step.properties.ModelArtifacts.S3ModelArtifacts,
                destination="/opt/ml/processing/model",
            ),
        ],
        outputs=[ProcessingOutput(output_name="evaluation", source="/opt/ml/processing/evaluation")],
        property_files=[eval_report],
    )
    return step, eval_report


# ── Step 4: Register model + update endpoint if AUC passes threshold ─────────

def condition_step(
    train_step: TrainingStep,
    eval_step: ProcessingStep,
    eval_report: PropertyFile,
) -> ConditionStep:

    register = RegisterModel(
        name="RegisterFraudModel",
        estimator=train_step.estimator,
        model_data=train_step.properties.ModelArtifacts.S3ModelArtifacts,
        content_types=["text/csv"],
        response_types=["application/json"],
        model_package_group_name=MODEL_PKG_GROUP,
    )

    return ConditionStep(
        name="CheckAUC",
        conditions=[
            ConditionGreaterThan(
                left=eval_report.get_step_property("auc"),
                right=auc_threshold,
            )
        ],
        if_steps=[register],
        else_steps=[],   # discard — keep current endpoint
    )


# ── Assemble and upsert pipeline ─────────────────────────────────────────────

def build_pipeline() -> Pipeline:
    export_step          = feature_export_step()
    train_step           = training_step(export_step)
    eval_step, eval_prop = evaluation_step(train_step)
    cond_step            = condition_step(train_step, eval_step, eval_prop)

    return Pipeline(
        name="FraudXGBoostRetrainingPipeline",
        parameters=[auc_threshold, instance_type],
        steps=[export_step, train_step, eval_step, cond_step],
        sagemaker_session=session,
    )


if __name__ == "__main__":
    pipeline = build_pipeline()
    pipeline.upsert(role_arn=role)
    print("Pipeline upserted: FraudXGBoostRetrainingPipeline")

    # Trigger a run immediately (EventBridge will call this weekly)
    execution = pipeline.start()
    print(f"Execution started: {execution.arn}")
