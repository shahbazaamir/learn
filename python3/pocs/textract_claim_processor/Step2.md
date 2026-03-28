# Step 2 — Claim Adjudication Engine

## What Step 2 Does

Step 1 (`claim_processor.py`) extracts raw structured data from the PDF via Textract.

Step 2 (`claim_adjudicator.py`) takes that extracted JSON and makes a **real business decision**:

| Task | Description |
|------|-------------|
| Policy lookup | Fetch patient's policy from DynamoDB |
| Validation | Check claim date, patient age, diagnosis coverage |
| Provider check | Verify hospital is an approved network provider |
| Payout calculation | Apply coverage %, deductible, max payout cap per line item |
| Persistence | Write decision to DynamoDB (fast lookup) + Aurora (audit/BI) |

---

## Production Use Case

A health insurer processes **~8,000 claims/day**. Before this pipeline, each claim took
2–5 business days with manual data entry. With Steps 1 + 2:

- **Auto-approved** claims (clean scan + valid policy + network provider): settled in **< 30 seconds**
- **Pending review** claims (low-confidence Textract fields): routed to A2I, settled same day
- **Rejected** claims: instant rejection notice with specific reason codes sent to hospital

---

## End-to-End Flow (Step 1 → Step 2)

```mermaid
flowchart TD
    A[Hospital uploads\nCLM-1042.pdf to S3] --> B[Step 1: claim_processor.py\nTextract extraction]
    B -->|confidence ≥ 80%\nstatus: AUTO_APPROVED| C[SNS: ClaimProcessed]
    B -->|confidence < 80%\nstatus: PENDING_REVIEW| D[A2I Human Review]
    D -->|reviewer corrects fields| C
    C --> E[Step 2: claim_adjudicator.py\nTriggered by SNS]
    E --> F[DynamoDB\nPolicy Table\nlookup policy rules]
    E --> G[DynamoDB\nProvider Table\ncheck network hospital]
    F --> H{Validate\nclaim date, age,\ndiagnosis code}
    G --> H
    H -->|invalid| I[REJECTED\nreason codes]
    H -->|valid| J[Calculate Payout\ncoverage % − deductible\ncapped at max]
    J --> K[APPROVED\nnet payout amount]
    I --> L[DynamoDB Claims Table\nfast status lookup]
    K --> L
    I --> M[Aurora Serverless\nfull audit trail + BI]
    K --> M

    style A fill:#f0f0f0,stroke:#aaa
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#fff3cd,stroke:#ffc107
    style E fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#1a73e8,color:#fff,stroke:#1a73e8
    style G fill:#1a73e8,color:#fff,stroke:#1a73e8
    style H fill:#fff3cd,stroke:#ffc107
    style I fill:#f8d7da,stroke:#dc3545
    style J fill:#d4edda,stroke:#28a745
    style K fill:#d4edda,stroke:#28a745
    style L fill:#1a73e8,color:#fff,stroke:#1a73e8
    style M fill:#1a73e8,color:#fff,stroke:#1a73e8
```

---

## Step 2 Internal Logic

```mermaid
flowchart LR
    A[SNS Message\nclaim JSON] --> B{status ==\nPENDING_REVIEW?}
    B -->|Yes| C[Skip — return 202\nA2I will re-trigger]
    B -->|No| D[get_policy\nDynamoDB lookup]
    D --> E[validate\nclaim date / age / diagnosis]
    E --> F[check_provider\nnetwork hospital?]
    F --> G{Any\nrejection reasons?}
    G -->|Yes| H[status: REJECTED]
    G -->|No| I[calculate_payout\nper line item]
    I --> J[status: APPROVED\nnet_payout]
    H --> K[persist_decision\nDynamoDB + Aurora]
    J --> K
```

---

## Payout Calculation Logic

```
gross_payout  = sum(procedure_costs) × coverage_percentage
net_payout    = min(gross_payout − deductible, max_payout_cap)
```

Example from `CLM-1042`:

| Field | Value |
|-------|-------|
| Total billed | $450.50 |
| Coverage % | 80% |
| Gross payout | $360.40 |
| Deductible | $100.00 |
| Net payout | **$260.40** |

---

## Data Stores

```mermaid
flowchart LR
    A[claim_adjudicator] -->|get_item| B[(DynamoDB\nPolicy Table\nPK: policy_number)]
    A -->|get_item| C[(DynamoDB\nProvider Table\nPK: hospital_name)]
    A -->|put_item| D[(DynamoDB\nClaims Table\nPK: claim_id\nfast status API)]
    A -->|INSERT| E[(Aurora Serverless\nclaim_decisions\nfull audit + BI + reporting)]
```

| Store | Why |
|-------|-----|
| DynamoDB Policy Table | Sub-millisecond policy lookup at scale |
| DynamoDB Provider Table | Fast in-network check per claim |
| DynamoDB Claims Table | Real-time status API for hospital portal |
| Aurora Serverless | SQL audit trail, finance reporting, BI dashboards |

---

## Validation Rules

| Rule | Rejection Code |
|------|---------------|
| Claim date outside policy active period | `CLAIM_DATE_OUTSIDE_POLICY_PERIOD` |
| Patient age outside covered range | `PATIENT_AGE_NOT_COVERED` |
| Diagnosis code in exclusion list | `DIAGNOSIS_NOT_COVERED: <code>` |
| Hospital not in approved network | `PROVIDER_NOT_IN_NETWORK` |

---

## Infrastructure

| Resource | Config |
|----------|--------|
| Lambda (Step 2) | `claim_adjudicator.py`, 512MB, timeout 30s |
| Trigger | SNS topic `ClaimProcessed` |
| DynamoDB Policy Table | On-demand, GSI on `patient_id` |
| DynamoDB Provider Table | On-demand |
| DynamoDB Claims Table | On-demand, TTL 90 days |
| Aurora Serverless v2 | PostgreSQL, min 0.5 ACU, max 4 ACU |

---

## IAM Permissions (Step 2 Lambda Role)

```json
{
  "Effect": "Allow",
  "Action": [
    "dynamodb:GetItem",
    "dynamodb:PutItem",
    "rds-data:ExecuteStatement",
    "secretsmanager:GetSecretValue",
    "sns:Subscribe"
  ],
  "Resource": "*"
}
```

---

## Project Structure (Full)

```
textract_claim_processor/
├── claim_processor.py      # Step 1: Textract extraction + A2I routing
├── claim_adjudicator.py    # Step 2: Validation + payout + persistence
├── ARCHITECTURE.md         # Step 1 architecture
├── Step2.md                # This file
├── CLAIM_PROCESSOR_DESIGN.md
├── EXAMPLE.md
├── README.md
└── requirements.txt
```
