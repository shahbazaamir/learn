# Support Ticket Classifier — BERT Fine-Tuning Architecture

## Use Case

A SaaS company receives **~15,000 support tickets/day** across email, chat, and web form.
Manual triage takes 8–12 minutes per ticket. Misrouted tickets add 2–3 hours of resolution delay.

Fine-tuned BERT classifies each ticket in **<200ms** with **>93% accuracy** on both:
- **Category:** Billing / Technical / Shipping / Returns
- **Urgency:** High / Medium / Low

High-urgency tickets trigger immediate SNS alerts to on-call agents.

---

## Why BERT (Fine-Tuning, Not Feature Extraction)

```mermaid
flowchart LR
    A[bert-base-uncased    Pre-trained on    BooksCorpus + Wikipedia] -->|fine-tune ALL layers    lr = 2e-5, 4 epochs| B[Domain-adapted BERT    understands support language    error codes, product names]
    B --> C[CLS token    768-dim representation]
    C --> D[Category Head    Linear 768→4    Billing/Technical    Shipping/Returns]
    C --> E[Urgency Head    Linear 768→3    High/Medium/Low]
```

Feature extraction would freeze BERT — it wouldn't learn domain-specific terms like
"payment failed", "kernel panic", "tracking number". Fine-tuning adapts all layers.

---

## End-to-End Architecture

```mermaid
flowchart TD
    A[Customer submits    support ticket] -->|POST /ticket| B[API Gateway]
    B --> C[Lambda    ticket_classifier_pipeline    lambda_handler]
    C -->|invoke_endpoint    text JSON| D[SageMaker Endpoint    support-ticket-classifier    Fine-tuned BERT]
    D -->|category + urgency    + confidence scores| C
    C --> E{Route by category}
    E -->|Billing| F[SQS: Billing Queue]
    E -->|Technical| G[SQS: Technical Queue]
    E -->|Shipping| H[SQS: Shipping Queue]
    E -->|Returns| I[SQS: Returns Queue]
    C -->|urgency == High| J[SNS: Agent Notify    immediate alert]
    C --> K[(DynamoDB    Tickets Table    classification log)]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style F fill:#1a73e8,color:#fff,stroke:#1a73e8
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
    style H fill:#1a73e8,color:#fff,stroke:#1a73e8
    style I fill:#1a73e8,color:#fff,stroke:#1a73e8
    style J fill:#f8d7da,stroke:#dc3545
    style K fill:#1a73e8,color:#fff,stroke:#1a73e8
```

---

## Training Pipeline

```mermaid
flowchart TD
    A[Labelled tickets CSV    text, category, urgency] -->|upload| B[S3 train/ + val/]
    B --> C[SageMaker HuggingFace    Training Job    ml.p3.2xlarge GPU    bert-base-uncased]
    C --> D[scripts/train.py    Dual-head fine-tuning    4 epochs, lr=2e-5    warmup + weight decay]
    D -->|cat_acc + urg_acc    per epoch| E[CloudWatch Metrics]
    D -->|bert weights    + heads.pt → S3| F[model.tar.gz]
    F --> G[HuggingFaceModel    deploy endpoint    ml.m5.xlarge]
```

---

## Dual-Head Model Design

```mermaid
flowchart LR
    A[Ticket text    max 128 tokens] --> B[BertTokenizer    input_ids    attention_mask]
    B --> C[BertModel    12 transformer layers    all layers fine-tuned]
    C -->|pooler_output    768-dim CLS vector| D[Dropout 0.1]
    D --> E[cat_head    Linear 768→4    Softmax]
    D --> F[urg_head    Linear 768→3    Softmax]
    E --> G[Category + confidence]
    F --> H[Urgency + confidence]
```

Single forward pass returns both outputs — no extra latency for dual classification.

---

## Inference Response

```json
{
  "category": "Technical",
  "urgency": "High",
  "category_confidence": 0.94,
  "urgency_confidence": 0.87,
  "category_scores": {
    "Billing": 0.02, "Technical": 0.94, "Shipping": 0.02, "Returns": 0.02
  },
  "urgency_scores": {
    "High": 0.87, "Medium": 0.11, "Low": 0.02
  }
}
```

---

## AWS Services

| Service | Role |
|---------|------|
| SageMaker HuggingFace Estimator | Fine-tune BERT on GPU (`ml.p3.2xlarge`) |
| SageMaker HuggingFace Endpoint | Real-time inference (`ml.m5.xlarge`) |
| API Gateway | Customer-facing ticket submission REST API |
| Lambda | Classify → route → notify orchestration |
| SQS (×4) | Per-category team queues with urgency message attributes |
| SNS | Immediate on-call alert for High urgency tickets |
| DynamoDB | Classification log — ticket_id, category, urgency, confidence |
| CloudWatch | Training metrics, endpoint latency alarms |

---

## Latency

| Step | Time |
|------|------|
| Tokenization | ~2ms |
| BERT forward pass (CPU `ml.m5.xlarge`) | ~120–180ms |
| SQS + DynamoDB writes | ~10ms |
| **Total p50** | **~140–200ms** |

Switch to `ml.g4dn.xlarge` (GPU) → BERT drops to ~15ms → total ~30ms.

---

## Project Structure

```
bert/
├── ticket_classifier_pipeline.py   # training + deployment + Lambda handler
├── scripts/
│   ├── train.py                    # dual-head BERT fine-tuning
│   └── inference.py                # SageMaker endpoint entry point
└── ARCHITECTURE.md
```

---

## IAM Permissions

```json
{
  "Effect": "Allow",
  "Action": [
    "sagemaker:CreateTrainingJob",
    "sagemaker:CreateModel",
    "sagemaker:CreateEndpoint",
    "sagemaker:InvokeEndpoint",
    "sqs:SendMessage",
    "sns:Publish",
    "dynamodb:PutItem",
    "s3:GetObject",
    "s3:PutObject"
  ],
  "Resource": "*"
}
```
