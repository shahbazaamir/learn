# Product Defect Detection — Transfer Learning Architecture

## Use Case

A consumer electronics manufacturer inspects **~7,000 products/hour** on 3 assembly lines.
Each product is photographed by a camera every 500ms. Defective products must be
physically rejected within **200ms** of detection.

Training a vision model from scratch requires millions of labelled images.
Transfer learning (ResNet-50 pre-trained on ImageNet) achieves **>92% accuracy
with only ~500 labelled defect images** per class — collected in 2 weeks vs 18 months.

**Defect classes:** OK / SCRATCH / DENT / DISCOLORATION

---

## End-to-End Architecture

```mermaid
flowchart TD
    A[Factory Camera    500ms capture interval] -->|image upload| B[S3    factory/line1/raw/]
    B -->|S3 Event| C[Lambda    defect_detection_pipeline    lambda_handler]
    C -->|invoke_endpoint| D[SageMaker Endpoint    product-defect-detector    ResNet-50 fine-tuned]
    D -->|prediction + confidence| C
    C -->|defect + conf > 0.80| E[AWS IoT Core    topic: factory/line1/reject]
    E -->|MQTT message| F[Factory PLC    physical rejection arm]
    C -->|all results| G[DynamoDB    Inspection Table    traceability log]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#f8d7da,stroke:#dc3545
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
```

---

## Transfer Learning — Why It Works Here

```mermaid
flowchart LR
    A[ImageNet    1.2M images    1000 classes    pre-trained weights] -->|freeze early layers    reuse feature extraction| B[ResNet-50    Convolutional Layers    edge / texture / shape detectors]
    B -->|replace final layer    fine-tune on| C[500 defect images    per class    SCRATCH / DENT etc]
    C --> D[Fine-tuned model    >92% accuracy    20 epochs on GPU]
```

Early layers detect universal features (edges, textures, shapes) — identical for product images.
Only the final classification layer is retrained on defect-specific patterns.

---

## Training Pipeline

```mermaid
flowchart TD
    A[Labelled images    data/images/OK    SCRATCH / DENT / DISCOLORATION] --> B[scripts/convert_to_recordio.py    resize 224×224    80/20 train/val split    → train.rec + val.rec]
    B -->|upload| C[S3 train/ + validation/]
    C --> D[SageMaker Training Job    defect-resnet50-finetune    ml.p3.2xlarge GPU    ResNet-50 + ImageNet weights]
    D -->|validation:accuracy| E{Accuracy    ≥ 92%?}
    E -->|Yes| F[Deploy Endpoint    ml.m5.xlarge    auto-scaling 1–4]
    E -->|No| G[Do not deploy    review training data]

    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#fff3cd,stroke:#ffc107
    style F fill:#d4edda,stroke:#28a745
    style G fill:#f8d7da,stroke:#dc3545
```

---

## Endpoint Auto-Scaling

```mermaid
flowchart LR
    A[SageMaker Endpoint    1 instance baseline] -->|invocations > 100/min| B[Scale Out    +1 instance    60s cooldown]
    B -->|invocations drop| C[Scale In    −1 instance    120s cooldown]
    C --> A
    A -.->|max| D[4 instances    ~2800 images/min capacity]
```

Handles burst traffic when multiple lines run simultaneously or shift changes cause spikes.

---

## Inference Decision Logic

```mermaid
flowchart TD
    A[Image bytes from S3] --> B[invoke_endpoint    ContentType: application/x-image]
    B --> C[Softmax scores    OK / SCRATCH / DENT / DISCOLORATION]
    C --> D[top_label = argmax    confidence = max score]
    D --> E{top_label != OK    AND confidence > 0.80?}
    E -->|Yes| F[Publish to IoT    factory/line1/reject    product_id + defect type]
    E -->|No| G[Product passes    no action]
    F --> H[DynamoDB log]
    G --> H
```

---

## AWS Services

| Service | Role |
|---------|------|
| S3 | Raw images, RecordIO dataset, model artifacts |
| SageMaker Training | ResNet-50 fine-tuning on GPU (`ml.p3.2xlarge`) |
| SageMaker Endpoint | Real-time inference, auto-scaling 1–4 instances |
| AWS Lambda | Orchestrates S3 → endpoint → IoT → DynamoDB |
| AWS IoT Core | MQTT message to factory PLC for physical rejection |
| DynamoDB | Per-product inspection log for traceability/audit |
| Application Auto Scaling | Scale endpoint on invocation rate |
| CloudWatch | Training metrics, endpoint latency alarms |

---

## Transfer Learning vs Training from Scratch

| | From Scratch | Transfer Learning |
|--|-------------|------------------|
| Images needed | ~100,000+ | ~500 per class |
| Training time | Days | ~45 min (20 epochs) |
| GPU cost | ~$200+ | ~$5 |
| Accuracy (500 images) | ~60% | >92% |
| Time to production | Months | Days |

---

## Project Structure

```
transfer_learning/
├── defect_detection_pipeline.py   # training + deployment + Lambda handler
├── scripts/
│   └── convert_to_recordio.py     # image → RecordIO conversion
└── ARCHITECTURE.md
```

---

## IAM Permissions

```json
{
  "Effect": "Allow",
  "Action": [
    "sagemaker:CreateTrainingJob",
    "sagemaker:CreateEndpoint",
    "sagemaker:InvokeEndpoint",
    "application-autoscaling:RegisterScalableTarget",
    "application-autoscaling:PutScalingPolicy",
    "iot:Publish",
    "dynamodb:PutItem",
    "s3:GetObject",
    "s3:PutObject",
    "cloudwatch:GetMetricStatistics"
  ],
  "Resource": "*"
}
```


This is fine-tuning, but it's worth being precise:

What the code does:
python
"use_pretrained_model": 1,   # load ImageNet weights
"epochs": 20,
"learning_rate": 0.001,


SageMaker's built-in Image Classification with use_pretrained_model=1 unfreezes and updates all layers during training — that's fine-tuning, not feature extraction.

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


The three types compared against this use case:

| Type | Layers trained | When to use | Our case? |
|------|---------------|-------------|-----------|
| Feature Extraction | Final layer only — backbone frozen | Very small dataset (<100 images), source & target domains similar | ❌ |
| Fine-Tuning | All layers (or last N layers) updated with small LR | Moderate dataset (~500+), target domain differs slightly from source | ✅ This one |
| Domain Adaptation | Specialized techniques (adversarial, MMD loss) to bridge large domain gap | Source and target domains are very different (e.g. photos → X-rays → satellite) | ❌ |

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


Why fine-tuning is the right choice here:

- ImageNet has no product/defect images — the domain gap is real, so freezing the backbone (feature extraction) would underperform
- ~500 images per class is enough to fine-tune without overfitting, especially with augmentation_type: crop_color_transform
- Domain adaptation would be overkill — the gap between ImageNet photos and product photos is moderate, not extreme

If the dataset were <100 images, feature extraction would be safer. If we were adapting from product photos to microscopic material scans, domain adaptation would be needed.

