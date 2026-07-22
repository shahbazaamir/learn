# Fraud Detection — Dimensionality Reduction & Visualization Architecture

## Use Case

A bank processes ~10M transactions/day. The fraud team needs to:
1. **Understand** what fraud looks like in high-dimensional feature space (50+ features)
2. **Validate** that fraud and legitimate transactions are separable before training a classifier
3. **Monitor** for concept drift — when fraud patterns shift, clusters visually diverge

Dimensionality reduction makes this possible by compressing 50 features into 2–3 dimensions for human interpretation.

---

## Why Each Technique

| Technique | Role in Production | When Used |
|-----------|-------------------|-----------|
| **PCA** | Fast linear compression, noise removal, speeds up downstream model | Pre-processing before training |
| **t-SNE** | Non-linear 2D visualization of transaction clusters | Exploratory analysis, drift detection |
| **LDA** | Maximizes class separability (fraud vs legit) — supervised | Validating feature quality before model training |

---

## End-to-End Architecture

```mermaid
flowchart TD
    A[Core Banking System    10M transactions/day] -->|Kinesis Firehose| B[S3 Raw Transactions    Parquet, partitioned by date]
    B -->|daily batch| C[SageMaker Processing Job    Feature Engineering]
    C --> D[S3 Feature Store    50 engineered features]
    D --> E1[PCA    Noise removal    50 → 15 components]
    D --> E2[t-SNE    2D cluster visualization    50 → 2 dims]
    D --> E3[LDA    Class separability check    50 → 1 dim]
    E1 -->|reduced features| F[SageMaker Training    XGBoost Fraud Classifier]
    E2 -->|2D coordinates| G[QuickSight Dashboard    Cluster Plot]
    E3 -->|separation score| H[Model Quality Gate    AUC + LDA score check]
    F --> I[SageMaker Endpoint    Real-time fraud scoring]
    H -->|pass| I
    H -->|fail| J[SNS Alert    Feature drift detected]

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E1 fill:#1a73e8,color:#fff,stroke:#1a73e8
    style E2 fill:#1a73e8,color:#fff,stroke:#1a73e8
    style E3 fill:#1a73e8,color:#fff,stroke:#1a73e8
    style F fill:#FF9900,color:#fff,stroke:#FF9900
    style G fill:#d4edda,stroke:#28a745
    style H fill:#fff3cd,stroke:#ffc107
    style I fill:#d4edda,stroke:#28a745
    style J fill:#f8d7da,stroke:#dc3545
```

---

## Processing Pipeline Detail

```mermaid
flowchart LR
    A[Raw Features    50 dims] --> B[Standardise    StandardScaler]
    B --> C1[PCA    sklearn.decomposition.PCA    retain 95% variance]
    B --> C2[t-SNE    sklearn.manifold.TSNE    perplexity=30, n_iter=1000]
    B --> C3[LDA    sklearn.discriminant_analysis    .LinearDiscriminantAnalysis]
    C1 -->|15 components| D1[Training features    CSV to S3]
    C2 -->|x, y coords + label| D2[Visualisation JSON    to S3 → QuickSight]
    C3 -->|Fisher score| D3[Separation metric    logged to CloudWatch]
```

---

## Drift Detection with t-SNE (Monthly)

```mermaid
flowchart TD
    A[Month N transactions    t-SNE plot] --> C{Cluster overlap    > threshold?}
    B[Month N+1 transactions    t-SNE plot] --> C
    C -->|No overlap increase| D[Patterns stable    no retraining needed]
    C -->|Overlap increased| E[Fraud pattern shifted    trigger retraining pipeline]
    E --> F[New Data Wrangler job    + retrain XGBoost]
```

---

## Feature Space — What Gets Reduced

| Feature Group | Count | Examples |
|---------------|-------|---------|
| Transaction metadata | 8 | amount, currency, channel, time_of_day |
| Merchant signals | 7 | merchant_category, country_risk_score |
| Velocity features | 12 | txn_count_1h, amount_sum_24h, unique_merchants_7d |
| Behavioural baseline | 15 | deviation from customer avg amount, location entropy |
| Device / network | 8 | device_age, ip_risk_score, vpn_flag |
| **Total** | **50** | → PCA reduces to 15, t-SNE to 2 |

---

## Infrastructure

| Component | Service | Config |
|-----------|---------|--------|
| Ingestion | Kinesis Firehose | Buffer: 5min / 128MB |
| Storage | S3 + Glue Catalog | Parquet, partitioned `year/month/day` |
| Feature engineering | SageMaker Processing | `ml.m5.4xlarge` × 2 |
| PCA / LDA | SageMaker Processing | `ml.m5.2xlarge` × 1 |
| t-SNE | SageMaker Processing | `ml.m5.4xlarge` × 1 (CPU-intensive) |
| Training | SageMaker Training | `ml.m5.2xlarge` × 1 |
| Endpoint | SageMaker Endpoint | `ml.m5.large`, auto-scale 2–8 |
| Visualisation | Amazon QuickSight | SPICE dataset refreshed daily |
| Alerting | SNS + CloudWatch | LDA Fisher score threshold alarm |

---

## Production Value

- **PCA** cuts training time by ~60% (50 → 15 features) with <2% AUC loss
- **t-SNE** lets fraud analysts visually confirm new fraud rings before they hit the model
- **LDA** acts as a cheap pre-check — if Fisher score drops, features have degraded and retraining is triggered automatically, without waiting for model AUC to degrade in production
