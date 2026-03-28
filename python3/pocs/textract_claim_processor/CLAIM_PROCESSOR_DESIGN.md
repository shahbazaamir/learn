# Insurance Claim Processor — Architecture & Deployment

## Use Case

Patients or hospitals upload insurance claim forms (PDFs / scanned images) to S3.
The system automatically extracts structured data, routes low-confidence scans to
human reviewers, and notifies downstream systems.

---

## High-Level Architecture

```mermaid
flowchart TD
    A[Hospital / Patient\nuploads claim form] -->|PUT s3://claims-bucket/CLM-001.pdf| B[S3 Bucket\nclaims-bucket]
    B -->|S3 Event Notification| C[AWS Lambda\nclaim_processor.py]
    C -->|StartDocumentAnalysis| D[AWS Textract\nFORMS + TABLES]
    D -->|Blocks + Confidence scores| C
    C -->|confidence ≥ 80%| E[Auto-Approved\nSNS Notification]
    C -->|confidence < 80%| F[Amazon A2I\nHuman Review Loop]
    F -->|Reviewer approves / corrects| G[Corrected Claim Data]
    E --> H[Downstream System\ne.g. Claims DB / ERP]
    G --> H

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#d4edda,stroke:#28a745
    style F fill:#fff3cd,stroke:#ffc107
    style G fill:#d4edda,stroke:#28a745
    style H fill:#cce5ff,stroke:#004085
```

---

## Lambda Internal Design

```mermaid
flowchart LR
    A[S3 Event] --> B[start_extraction\nStartDocumentAnalysis]
    B --> C[wait_for_job\npoll GetDocumentAnalysis]
    C --> D[parse_kv\nkey-value + confidence]
    C --> E[parse_tables\nitemised procedures]
    D --> F{Any field\nconfidence < 80%?}
    F -->|Yes| G[maybe_send_to_human_review\nA2I Human Loop]
    F -->|No| H[status: AUTO_APPROVED]
    G --> I[status: PENDING_REVIEW]
    H --> J[notify via SNS]
    I --> J
```

---

## Textract Advantages Mapped to This Project

| Advantage | Where used |
|-----------|-----------|
| No ML expertise | Managed `analyze_document` API — no model training |
| Beyond OCR | `FORMS` feature extracts `Patient Name`, `Policy No`, `Date` as KV pairs |
| Table extraction | `TABLES` feature extracts itemised procedure/cost rows |
| Complex layouts | Handles multi-column claim forms, checkboxes, handwritten fields |
| Async processing | `StartDocumentAnalysis` for multi-page PDFs — no Lambda timeout |
| Native AWS integration | S3 trigger → Lambda → Textract → SNS → A2I |
| Confidence scores | Fields below 80% routed to human review automatically |
| Structured output | Returns typed `claim` dict with per-field confidence |

---

## Deployment Pipeline

```mermaid
flowchart LR
    A[Developer pushes code\nto GitHub] --> B[GitHub Actions CI]
    B --> C[Run tests\npytest]
    C --> D[Package Lambda\nzip claim_processor.py]
    D --> E[aws lambda update-function-code]
    E --> F[Lambda deployed\nin AWS]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#24292e,color:#fff,stroke:#24292e
    style C fill:#2ea44f,color:#fff,stroke:#2ea44f
    style D fill:#f0f0f0,stroke:#aaa
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#d4edda,stroke:#28a745
```

### Manual Deployment Steps

```bash
# 1. Package
zip claim_processor.zip claim_processor.py

# 2. Create Lambda (first time)
aws lambda create-function \
  --function-name ClaimProcessor \
  --runtime python3.12 \
  --role arn:aws:iam::<account-id>:role/ClaimProcessorRole \
  --handler claim_processor.lambda_handler \
  --zip-file fileb://claim_processor.zip \
  --timeout 300

# 3. Update Lambda (subsequent deploys)
aws lambda update-function-code \
  --function-name ClaimProcessor \
  --zip-file fileb://claim_processor.zip

# 4. Add S3 trigger
aws s3api put-bucket-notification-configuration \
  --bucket claims-bucket \
  --notification-configuration file://s3-trigger.json
```

### Required IAM Permissions (Lambda Role)

```json
{
  "Effect": "Allow",
  "Action": [
    "textract:StartDocumentAnalysis",
    "textract:GetDocumentAnalysis",
    "s3:GetObject",
    "sns:Publish",
    "sagemaker:StartHumanLoop"
  ],
  "Resource": "*"
}
```

### `s3-trigger.json`

```json
{
  "LambdaFunctionConfigurations": [{
    "LambdaFunctionArn": "arn:aws:lambda:us-east-1:<account-id>:function:ClaimProcessor",
    "Events": ["s3:ObjectCreated:*"],
    "Filter": {
      "Key": { "FilterRules": [{ "Name": "prefix", "Value": "claims/" }] }
    }
  }]
}
```
