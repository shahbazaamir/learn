# Customer Churn Prediction — Architecture & Design

## Use Case

A telecom company has ~2M customers. Every month, ~5% churn (cancel subscription).
Retaining a customer costs 5x less than acquiring a new one.

This pipeline automatically scores every customer monthly and flags high-risk ones
for the retention team to act on.

---

## End-to-End Architecture

```mermaid
flowchart TD
    A[CRM / Billing System\nRaw customer data] -->|monthly export| B[S3 Raw Data\ncustomer_data.csv]
    B --> C[SageMaker Data Wrangler\nchurn.flow]
    C -->|ProcessingJob| D[S3 Processed Data\ntrain / validation splits]
    D --> E[SageMaker Training Job\nXGBoost]
    E -->|model artifact| F[S3 Model\nxgboost-model.tar.gz]
    F --> G[SageMaker Endpoint\nchurn-predictor\nreal-time]
    F --> H[SageMaker Batch Transform\nmonthly scoring]
    G -->|churn score| I[Retention App\nAPI calls per customer]
    H -->|bulk scores CSV| J[S3 Batch Output\n→ BI Dashboard / CRM]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#FF9900,color:#fff,stroke:#FF9900
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
    style H fill:#1a73e8,color:#fff,stroke:#1a73e8
    style I fill:#d4edda,stroke:#28a745
    style J fill:#d4edda,stroke:#28a745
```

---

## Data Wrangler Transformation Flow

```mermaid
flowchart LR
    A[Raw CSV\n15 columns] --> B[Drop nulls\ntenure, monthly_charges]
    B --> C[Encode categoricals\ncontract_type, payment_method]
    C --> D[Feature engineering\navg_charge_per_month\nsupport_calls_ratio]
    D --> E[Balance classes\nSMOTE oversampling]
    E --> F[Train / Validation split\n80 / 20]
    F --> G[S3 Processed Data]
```

### Key Transformations in `.flow`

| Step | Transformation | Why |
|------|---------------|-----|
| 1 | Drop rows where `tenure` or `monthly_charges` is null | Required features |
| 2 | One-hot encode `contract_type` (month-to-month, 1yr, 2yr) | XGBoost needs numeric |
| 3 | One-hot encode `payment_method` | Same |
| 4 | `avg_charge_per_month = total_charges / tenure` | Derived signal |
| 5 | `support_calls_ratio = support_calls / tenure` | Frustration signal |
| 6 | SMOTE on minority class (churned=1) | Class imbalance ~5% |
| 7 | 80/20 stratified split | Preserve churn ratio |

---

## Model Design

| Component | Choice | Reason |
|-----------|--------|--------|
| Algorithm | XGBoost | Industry standard for tabular churn, fast, interpretable |
| Metric | AUC-ROC | Handles class imbalance better than accuracy |
| Threshold | 0.35 | Tuned for high recall — better to over-flag than miss churners |
| Retraining | Monthly | Triggered by new data arriving in S3 |

---

## Deployment Design

```mermaid
flowchart TD
    A[New month data arrives in S3] -->|EventBridge rule| B[Lambda: trigger pipeline]
    B --> C[Data Wrangler Processing Job]
    C --> D[XGBoost Training Job]
    D --> E{AUC > 0.85?}
    E -->|Yes| F[Update Endpoint\nchurn-predictor]
    E -->|No| G[Alert SNS\nModel quality degraded]
    F --> H[Run Batch Transform\nScore all 2M customers]
    H --> I[Results → S3 → CRM Dashboard]
```

### Infrastructure

| Resource | Config |
|----------|--------|
| Processing Job | `ml.m5.4xlarge` × 1 |
| Training Job | `ml.m5.xlarge` × 1 |
| Endpoint | `ml.m5.large` × 1 (auto-scaling 1–4) |
| Batch Transform | `ml.m5.xlarge` × 2 |
| Trigger | EventBridge cron: `0 2 1 * *` (1st of month, 2am) |

---

## IAM Permissions Required

```json
{
  "Effect": "Allow",
  "Action": [
    "sagemaker:CreateProcessingJob",
    "sagemaker:CreateTrainingJob",
    "sagemaker:CreateTransformJob",
    "sagemaker:CreateEndpoint",
    "sagemaker:UpdateEndpoint",
    "s3:GetObject",
    "s3:PutObject",
    "sns:Publish"
  ],
  "Resource": "*"
}
```

---

## Project Structure

```
sagemaker_churn/
├── churn_pipeline.py     # orchestrates full pipeline
├── train.py              # XGBoost training entry point
├── churn.flow            # Data Wrangler flow (exported from UI)
└── ARCHITECTURE.md
```
