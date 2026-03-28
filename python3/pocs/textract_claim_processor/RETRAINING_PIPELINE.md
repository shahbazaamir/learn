# SageMaker Retraining Pipeline — Fraud XGBoost

## Purpose

`fraud_retraining_pipeline.py` defines a weekly SageMaker Pipeline that:
1. Reads analyst-labelled records from SageMaker Feature Store (written by `analyst_label_updater.py`)
2. Retrains the XGBoost fraud classifier
3. Evaluates the new model AUC
4. Registers and deploys the new model **only if** AUC exceeds the current threshold
5. The updated endpoint is the same one called by `claim_fraud_scorer.py`

---

## Pipeline Steps

```mermaid
flowchart TD
    A[EventBridge Cron    Every Monday 02:00 UTC] --> B[SageMaker Pipeline    FraudXGBoostRetrainingPipeline]
    B --> C[Step 1: ExportFeatureStore    ScriptProcessor    export_features.py    filter label = FRAUD or LEGIT]
    C -->|train.csv    validation.csv → S3| D[Step 2: TrainFraudXGBoost    XGBoost Estimator    ml.m5.xlarge]
    D -->|model artifact → S3| E[Step 3: EvaluateModel    ScriptProcessor    evaluate.py    compute AUC on validation set]
    E -->|evaluation.json    auc score| F{Step 4: CheckAUC    New AUC >    auc_threshold 0.85?}
    F -->|Yes| G[RegisterModel    Model Package Group    fraud-xgboost-models]
    G --> H[Update SageMaker Endpoint    claim_fraud_scorer.py    calls this endpoint]
    F -->|No| I[Discard model    keep current endpoint]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#fff3cd,stroke:#ffc107
    style G fill:#d4edda,stroke:#28a745
    style H fill:#d4edda,stroke:#28a745
    style I fill:#f8d7da,stroke:#dc3545
```

---

## Data Flow Into the Pipeline

```mermaid
flowchart LR
    A[claim_fraud_scorer.py    stores label = UNKNOWN] --> B[(SageMaker    Feature Store    Online + Offline)]
    C[analyst_label_updater.py    updates label = FRAUD or LEGIT] --> B
    B -->|offline store S3 export| D[Step 1: ExportFeatureStore    filter confirmed labels only]
    D --> E[Step 2: Train XGBoost    on labelled data]
```

---

## Pipeline Parameters

| Parameter | Default | Description |
|-----------|---------|-------------|
| `AucThreshold` | `0.85` | Minimum AUC for new model to replace current |
| `InstanceType` | `ml.m5.xlarge` | Training instance — override for larger datasets |

---

## How It Connects to the Rest of the Project

```mermaid
flowchart TD
    A[analyst_label_updater.py    FRAUD / LEGIT labels] -->|Feature Store| B[fraud_retraining_pipeline.py    weekly retraining]
    B -->|updated endpoint| C[claim_fraud_scorer.py    xgboost_score calls    XGBOOST_ENDPOINT]
    C -->|score > 0.7| D[SQS Fraud Queue    held claims]
    D --> A

    style A fill:#FF9900,color:#fff,stroke:#FF9900
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#fff3cd,stroke:#ffc107
```

The loop: analyst labels → Feature Store → retrain → better endpoint → fewer false positives → fewer claims for analysts to review.

---

## Infrastructure

| Resource | Config |
|----------|--------|
| Pipeline trigger | EventBridge cron: `cron(0 2 ? * MON *)` |
| Step 1 — Export | `ml.m5.large` × 1, ScriptProcessor |
| Step 2 — Train | `ml.m5.xlarge` × 1, XGBoost 1.7-1 built-in image |
| Step 3 — Evaluate | `ml.m5.large` × 1, ScriptProcessor |
| Model registry | SageMaker Model Package Group: `fraud-xgboost-models` |
| Endpoint updated | Same `XGBOOST_ENDPOINT` used by `claim_fraud_scorer.py` |

---

## IAM Permissions Required

```json
{
  "Effect": "Allow",
  "Action": [
    "sagemaker:CreatePipeline",
    "sagemaker:StartPipelineExecution",
    "sagemaker:CreateTrainingJob",
    "sagemaker:CreateProcessingJob",
    "sagemaker:RegisterModel",
    "sagemaker:UpdateEndpoint",
    "sagemaker-featurestore-runtime:GetRecord",
    "s3:GetObject",
    "s3:PutObject"
  ],
  "Resource": "*"
}
```
