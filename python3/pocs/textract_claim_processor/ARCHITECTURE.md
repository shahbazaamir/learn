# Architecture Diagram

## Flow: S3 → Lambda → Textract → Output

```mermaid
flowchart TD
    A[User uploads document] -->|PUT object| B[S3 Bucket]
    B -->|S3 Event Trigger| C[AWS Lambda\nlambda_extractor.py]
    C -->|analyze_document| D[AWS Textract]
    D -->|Blocks / KV pairs| C
    C -->|Extracted JSON| E[Response / Downstream]

    style A fill:#f0f0f0,stroke:#999
    style B fill:#FF9900,color:#fff,stroke:#FF9900
    style C fill:#FF9900,color:#fff,stroke:#FF9900
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style E fill:#d4edda,stroke:#28a745
```

## Component Roles

| Component | Role |
|-----------|------|
| S3 Bucket | Stores uploaded documents (images, PDFs) |
| Lambda | Triggered on upload; orchestrates extraction |
| Textract | Reads document, returns key-value form fields |
| Response | JSON with extracted field names and values |

## Lambda Internal Flow

```mermaid
flowchart LR
    A[S3 Event] --> B[Parse bucket & key]
    B --> C[Call Textract analyze_document]
    C --> D[Filter KEY_VALUE_SET blocks]
    D --> E[Resolve child WORD blocks]
    E --> F[Return extracted dict]
```
