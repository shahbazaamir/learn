# Medical X-Ray Labelling Pipeline — Architecture & Design

## Use Case

A hospital network builds a **pneumonia detection AI** from chest X-rays.
They have 50,000 unlabelled X-ray images. The challenge:

- Raw images include corrupt files, non-X-ray images, poor quality scans
- Only radiologists can provide medically valid labels
- Radiologist time is expensive — can't label everything manually
- Deployed model needs human oversight for uncertain predictions

This pipeline solves all four problems using Ground Truth, Mechanical Turk, private workforce, and A2I.

---

## High-Level Architecture

```mermaid
flowchart TD
    A[Hospital PACS System    50k chest X-rays] -->|batch upload| B[S3 Raw Images    xray/raw/]
    B --> C[Ground Truth Job 1    Mechanical Turk    Quality Screening]
    C -->|valid images only    output.manifest| D[Ground Truth Job 2    Private Workforce    Radiologist Labelling]
    D -->|labelled manifest| E[SageMaker Training    Image Classifier    Transfer Learning]
    E -->|model artifact| F[SageMaker Endpoint    pneumonia-detector]
    F -->|confidence ≥ 0.85| G[Auto Result    Normal / Pneumonia]
    F -->|confidence < 0.85| H[A2I Human Loop    Radiologist Review]
    H -->|confirmed label| I[SageMaker Feature Store    feedback for retraining]
    I -->|periodic| E

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#232F3E,color:#fff,stroke:#232F3E
    style D fill:#1a73e8,color:#fff,stroke:#1a73e8
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#FF9900,color:#fff,stroke:#FF9900
    style G fill:#d4edda,stroke:#28a745
    style H fill:#fff3cd,stroke:#ffc107
    style I fill:#FF9900,color:#fff,stroke:#FF9900
```

---

## Diagram 2 — Ground Truth Job 1: MTurk Quality Screening

Purpose: filter out corrupt, non-medical, or low-quality images before expensive radiologist time is spent.

```mermaid
flowchart LR
    A[input.manifest    100 image S3 URIs] --> B[Ground Truth    Mechanical Turk Job    xray-quality-screening]
    B --> C[3 MTurk workers    per image    30 sec task]
    C --> D[ACS Lambda    Annotation Consolidation    majority vote]
    D -->|label = valid| E[Passes to    Radiologist Job]
    D -->|label = invalid| F[Discarded    not labelled]
```

Workers answer one question: *"Is this a valid chest X-ray suitable for medical analysis?"*
Cost: ~$0.03/image × 3 workers = $0.09/image — cheap pre-filter before radiologist cost.

---

## Diagram 3 — Ground Truth Job 2: Radiologist Labelling

Purpose: expert medical labels on validated images only.

```mermaid
flowchart LR
    A[Screened manifest    valid images only] --> B[Ground Truth    Private Workforce Job    xray-pneumonia-labelling]
    B --> C[2 Radiologists    per image    5 min task]
    C --> D[ACS Lambda    Annotation Consolidation    agreement check]
    D -->|both agree| E[Label: Normal    or Pneumonia]
    D -->|disagree| F[Label: Uncertain    flagged for 3rd review]
    E --> G[output.manifest    with labels → S3]
    F --> G
```

---

## Diagram 4 — Model Training & Deployment

```mermaid
flowchart TD
    A[output.manifest    labelled X-rays] --> B[SageMaker Training Job    Image Classification    ml.p3.2xlarge GPU]
    B -->|transfer learning    from ImageNet| C[Fine-tuned model    3 classes: Normal / Pneumonia / Uncertain]
    C --> D[SageMaker Endpoint    pneumonia-detector    ml.m5.xlarge]
```

---

## Diagram 5 — A2I Production Review Flow

Runs at inference time for every prediction below 85% confidence.

```mermaid
flowchart TD
    A[New X-ray arrives    at endpoint] --> B[invoke_with_a2i_review    model prediction]
    B --> C{confidence    ≥ 0.85?}
    C -->|Yes| D[Return result    Normal or Pneumonia]
    C -->|No| E[A2I: start_human_loop    xray-review-id]
    E --> F[Radiologist reviews    image + model suggestion    in A2I portal]
    F --> G[Confirmed label    returned to caller]
    G --> H[SageMaker Feature Store    new labelled record    for retraining]
```

---

## Workforce Strategy

| Workforce | Task | Cost | Turnaround |
|-----------|------|------|-----------|
| Mechanical Turk | Quality screening — valid X-ray? | ~$0.09/image | Minutes |
| Private (Radiologists) | Medical labelling — Normal / Pneumonia | ~$2–5/image | Hours |
| A2I Private Loop | Review uncertain model predictions | ~$2–5/review | Same day |

---

## AWS Services Used

| Service | Role |
|---------|------|
| S3 | Raw images, manifests, model artifacts |
| SageMaker Ground Truth | Managed labelling jobs with workforce routing |
| Mechanical Turk | Public crowd for cheap quality pre-screening |
| SageMaker Private Workforce | HIPAA-eligible radiologist labelling team |
| SageMaker Image Classification | Built-in transfer learning algorithm |
| SageMaker Endpoint | Real-time inference |
| Amazon A2I | Human review loop for low-confidence predictions |
| SageMaker Feature Store | Stores A2I-confirmed labels for retraining |

---

## Project Structure

```
sagemaker_ground_truth/
├── xray_labelling_pipeline.py   # full pipeline orchestration
└── ARCHITECTURE.md              # this file
```

---

## IAM Permissions Required

```json
{
  "Effect": "Allow",
  "Action": [
    "sagemaker:CreateLabelingJob",
    "sagemaker:CreateTrainingJob",
    "sagemaker:CreateEndpoint",
    "sagemaker:InvokeEndpoint",
    "sagemaker-a2i-runtime:StartHumanLoop",
    "sagemaker-featurestore-runtime:PutRecord",
    "s3:GetObject",
    "s3:PutObject",
    "lambda:InvokeFunction"
  ],
  "Resource": "*"
}
```
