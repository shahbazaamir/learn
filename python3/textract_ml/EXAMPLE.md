# Example — Claim Processor in Action

## Uploaded Document

File: `s3://claims-bucket/claims/CLM-1042.pdf`
Type: Medical Insurance Claim Form (3 pages, scanned)

---

## Extracted Key-Value Fields

| Field | Value | Confidence |
|-------|-------|------------|
| Patient Name | John Carter | 97.4% |
| Date of Birth | 14/06/1985 | 95.1% |
| Policy Number | POL-887234 | 98.2% |
| Claim Date | 10/03/2026 | 96.8% |
| Diagnosis Code | ICD-J18.9 | 91.3% |
| Doctor Name | Dr. Sarah Mills | 88.6% |
| Hospital Name | City General Hospital | 93.0% |
| Signature | *(handwritten)* | 74.2% ⚠️ |

> ⚠️ `Signature` confidence < 80% — routed to **A2I human review**

---

## Extracted Table — Itemised Procedures

| Procedure | Code | Date | Cost |
|-----------|------|------|------|
| Chest X-Ray | RAD-101 | 10/03/2026 | $120.00 |
| Blood Test (CBC) | LAB-204 | 10/03/2026 | $85.00 |
| Consultation | CON-001 | 10/03/2026 | $200.00 |
| Medication | MED-512 | 11/03/2026 | $45.50 |
| **Total** | | | **$450.50** |

---

## Claim Output (JSON)

```json
{
  "claim_id": "CLM-1042",
  "status": "PENDING_REVIEW",
  "fields": {
    "Patient Name":   { "value": "John Carter",           "confidence": 97.4 },
    "Policy Number":  { "value": "POL-887234",            "confidence": 98.2 },
    "Diagnosis Code": { "value": "ICD-J18.9",             "confidence": 91.3 },
    "Signature":      { "value": "",                      "confidence": 74.2 }
  },
  "tables": [[
    ["Chest X-Ray",    "RAD-101", "10/03/2026", "$120.00"],
    ["Blood Test",     "LAB-204", "10/03/2026", "$85.00"],
    ["Consultation",   "CON-001", "10/03/2026", "$200.00"],
    ["Medication",     "MED-512", "11/03/2026", "$45.50"]
  ]]
}
```

---

## What Happened

1. `CLM-1042.pdf` uploaded to S3 → triggered Lambda
2. Textract ran async analysis (3 pages, FORMS + TABLES)
3. All fields extracted with confidence scores
4. `Signature` field fell below 80% threshold → sent to A2I for human review
5. SNS notification published with status `PENDING_REVIEW`
