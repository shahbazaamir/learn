# End-to-End Flow — Medical Insurance Claim Processor

## Project Files

| File | Role |
|------|------|
| `claim_processor.py` | Step 1 — Textract extraction + A2I routing |
| `claim_adjudicator.py` | Step 2 — Policy validation + payout calculation |
| `claim_fraud_scorer.py` | Step 3 — ML fraud scoring + payment routing |
| `billing_anomaly_detector_sagemaker.py` | Parallel — RCF billing anomaly detection |
| `analyst_label_updater.py` | Feedback — analyst labels FRAUD / LEGIT |

---

## Diagram 1 — High-Level Pipeline Overview

```mermaid
flowchart TD
    A[Hospital uploads\nclaim PDF to S3] --> B[Step 1\nclaim_processor.py]
    B --> C[Step 2\nclaim_adjudicator.py]
    C -->|APPROVED| D[Step 3\nclaim_fraud_scorer.py]
    C -->|REJECTED| E[Rejection Notice\nDynamoDB + Aurora]
    D -->|score ≤ 0.7| F[Payment Released\nStep Functions]
    D -->|score 0.7–0.9| G[Held for Review\nSQS Fraud Queue]
    D -->|score > 0.9| H[Auto Rejected\nSQS Fraud Queue]
    G --> I[analyst_label_updater.py\nFRAUD or LEGIT]
    I -->|label stored| J[SageMaker Feature Store\nretraining feedback]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#f8d7da,stroke:#dc3545
    style F fill:#d4edda,stroke:#28a745
    style G fill:#fff3cd,stroke:#ffc107
    style H fill:#f8d7da,stroke:#dc3545
    style I fill:#FF9900,color:#fff,stroke:#FF9900
    style J fill:#1a73e8,color:#fff,stroke:#1a73e8
```

---

## Diagram 2 — Step 1: Textract Extraction (`claim_processor.py`)

Triggered by S3 upload. Extracts structured fields and tables from the PDF.

```mermaid
flowchart TD
    A[S3: claim PDF uploaded] -->|S3 Event| B[Lambda: claim_processor]
    B --> C[Textract\nStartDocumentAnalysis\nFORMS + TABLES]
    C --> D[Poll GetDocumentAnalysis\nwait_for_job]
    D --> E[parse_kv\nkey-value fields\n+ confidence scores]
    D --> F[parse_tables\nitemised procedures]
    E --> G{Any field\nconfidence < 80%?}
    G -->|Yes| H[A2I Human Review\nmaybe_send_to_human_review]
    G -->|No| I[status: AUTO_APPROVED]
    H -->|reviewer corrects| I
    I --> J[SNS: ClaimProcessed\nnotify]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style H fill:#fff3cd,stroke:#ffc107
    style I fill:#d4edda,stroke:#28a745
    style J fill:#FF9900,color:#fff,stroke:#FF9900
```

---

## Diagram 3 — Step 2: Adjudication (`claim_adjudicator.py`)

Triggered by SNS. Validates claim against policy rules and calculates payout.

```mermaid
flowchart TD
    A[SNS: ClaimProcessed] -->|trigger| B[Lambda: claim_adjudicator]
    B --> C{status ==\nPENDING_REVIEW?}
    C -->|Yes| D[Skip — return 202\nA2I will re-trigger]
    C -->|No| E[get_policy\nDynamoDB Policy Table]
    E --> F[validate\nclaim date / patient age\ndiagnosis code]
    F --> G[check_provider\nDynamoDB Provider Table]
    G --> H{Rejection\nreasons?}
    H -->|Yes| I[status: REJECTED]
    H -->|No| J[calculate_payout\ncoverage % − deductible\ncapped at max_payout]
    J --> K[status: APPROVED]
    I --> L[persist_decision\nDynamoDB Claims Table]
    K --> L
    L --> M[Aurora Serverless\naudit trail + BI]
    K -->|EventBridge event| N[Step 3 triggered]

    style A fill:#FF9900,color:#fff,stroke:#FF9900
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#fff3cd,stroke:#ffc107
    style E fill:#1a73e8,color:#fff,stroke:#1a73e8
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
    style I fill:#f8d7da,stroke:#dc3545
    style K fill:#d4edda,stroke:#28a745
    style L fill:#1a73e8,color:#fff,stroke:#1a73e8
    style M fill:#1a73e8,color:#fff,stroke:#1a73e8
    style N fill:#FF9900,color:#fff,stroke:#FF9900
```

---

## Diagram 4 — Step 3: Fraud Scoring (`claim_fraud_scorer.py`)

Triggered by EventBridge after Step 2 APPROVED. Runs 3 ML signals in parallel.

```mermaid
flowchart TD
    A[EventBridge: APPROVED claim] --> B[Lambda: claim_fraud_scorer]
    B --> C[xgboost_score\nSageMaker Endpoint\nfeature vector inference]
    B --> D[duplicate_score\nBedrock Titan embed\nOpenSearch k-NN]
    B --> E[neptune_risk_score\nNeptune Gremlin\nprovider cluster score]
    C --> F[composite_score\n0.6×xgb + 0.25×dup\n+ 0.15×neptune]
    D --> F
    E --> F
    F --> G[store_feature_record\nSageMaker Feature Store\nlabel = UNKNOWN]
    F --> H{Score?}
    H -->|≤ 0.7| I[Step Functions\nrelease payment]
    H -->|0.7 – 0.9| J[SQS Fraud Queue\nHOLD]
    H -->|> 0.9| K[SQS Fraud Queue\nAUTO_REJECT]

    style A fill:#FF9900,color:#fff,stroke:#FF9900
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style D fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style E fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
    style I fill:#d4edda,stroke:#28a745
    style J fill:#fff3cd,stroke:#ffc107
    style K fill:#f8d7da,stroke:#dc3545
```

---

## Diagram 5 — Billing Anomaly Detection (`billing_anomaly_detector_sagemaker.py`)

Runs independently every 15 minutes via EventBridge. Detects inflated billing patterns across all recent claims in batch.

```mermaid
flowchart LR
    A[EventBridge\nevery 15 min] --> B[billing_anomaly_detector_sagemaker]
    B --> C[prepare_features\nload S3 CSV\nengineer billing features]
    C --> D[upload_train_data\nRecordIO-protobuf → S3]
    D --> E[train_rcf\nSageMaker RandomCutForest\nTraining Job]
    E --> F[score_claims\nRCF predictor\nanomaly scores]
    F --> G{score > mean\n+ 3×std?}
    G -->|Yes| H[save_anomalies\nflagged claim_ids → S3]
    G -->|No| I[Normal billing\nno action]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style H fill:#f8d7da,stroke:#dc3545
    style I fill:#d4edda,stroke:#28a745
```

---

## Diagram 6 — Analyst Feedback & Retraining Loop (`analyst_label_updater.py`)

Closes the ML feedback loop. Analyst decisions become training data for the next XGBoost model.

```mermaid
flowchart TD
    A[Analyst reviews\nheld claim in portal] -->|POST /review| B[API Gateway]
    B --> C[Lambda: analyst_label_updater]
    C --> D{label valid?\nFRAUD or LEGIT}
    D -->|No| E[400 Bad Request]
    D -->|Yes| F[SageMaker Feature Store\nput_record\nlabel = FRAUD or LEGIT]
    F --> G[SQS Fraud Queue\naction = RESOLVED]
    F --> H[Weekly retraining\nreads Feature Store\noffline store]
    H --> I[New XGBoost model\ntrained on labelled data]
    I --> J{New AUC >\ncurrent AUC?}
    J -->|Yes| K[Update SageMaker\nEndpoint]
    J -->|No| L[Discard model\nkeep current]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#f8d7da,stroke:#dc3545
    style F fill:#1a73e8,color:#fff,stroke:#1a73e8
    style G fill:#FF9900,color:#fff,stroke:#FF9900
    style H fill:#FF9900,color:#fff,stroke:#FF9900
    style I fill:#FF9900,color:#fff,stroke:#FF9900
    style K fill:#d4edda,stroke:#28a745
    style L fill:#fff3cd,stroke:#ffc107
```

---

## Data Stores Summary

```mermaid
flowchart LR
    A[claim_processor] -->|extracted JSON| B[SNS ClaimProcessed]
    C[claim_adjudicator] -->|reads| D[(DynamoDB\nPolicy Table)]
    C -->|reads| E[(DynamoDB\nProvider Table)]
    C -->|writes| F[(DynamoDB\nClaims Table)]
    C -->|writes| G[(Aurora Serverless\nclaim_decisions)]
    H[claim_fraud_scorer] -->|reads/writes| I[(SageMaker\nFeature Store)]
    J[analyst_label_updater] -->|updates label| I
    K[billing_anomaly_detector] -->|reads| L[(S3\nrecent_claims.csv)]
    K -->|writes| M[(S3\nanomalies.csv)]
```
