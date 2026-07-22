# Enterprise IT Operations Platform — Amazon Q Architecture

## Use Case

A 10,000-employee enterprise deploys all Amazon Q surfaces to solve four distinct problems:

| Problem | Amazon Q Service | Users |
|---------|-----------------|-------|
| Employees waste 2hrs/day searching IT docs | Q Business | All 10,000 employees |
| Engineers miss security issues in code reviews | Q Developer | 500 engineers |
| BI analysts wait days for custom reports | Q in QuickSight | 200 analysts |
| Support agents spend 8min searching KB per call | Q in Connect | 150 agents |

---

## High-Level Architecture — All Q Services

```mermaid
flowchart TD
    subgraph Employees
        A[Employee Portal    IT self-service chat]
    end
    subgraph Engineers
        B[VS Code / JetBrains    Inline code suggestions    Security scans in CI/CD]
    end
    subgraph Analysts
        C[QuickSight Dashboard    Natural language questions]
    end
    subgraph Agents
        D[Amazon Connect    Agent workspace    Real-time suggestions]
    end

    A -->|chat| E[Q Business    IT-HelpDesk-Assistant]
    B -->|code| F[Q Developer    CodeWhisperer API]
    C -->|NL query| G[Q in QuickSight    IT Operations Topic]
    D -->|conversation| H[Q in Connect    Wisdom Assistant]

    E --> I[(S3 IT Runbooks    + ServiceNow KB)]
    F --> J[Security findings    → CodePipeline gate]
    G --> K[(QuickSight Dataset    IT Tickets)]
    H --> L[(IT Agent KB    S3 + AppIntegrations)]

    style E fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style F fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style G fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style H fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style I fill:#FF9900,color:#fff,stroke:#FF9900
    style J fill:#FF9900,color:#fff,stroke:#FF9900
    style K fill:#FF9900,color:#fff,stroke:#FF9900
    style L fill:#FF9900,color:#fff,stroke:#FF9900
```

---

## Q Business — Employee IT Self-Service

```mermaid
flowchart TD
    A[Employee asks:    How do I connect to VPN    from a new laptop?] --> B[Q Business    IT-HelpDesk-Assistant]
    B --> C{Search index}
    C --> D[(S3: IT Runbooks    VPN setup guides    SOPs)]
    C --> E[(ServiceNow    Resolved tickets    Knowledge articles)]
    D --> F[Grounded answer    with source citations]
    E --> F
    F --> G[Employee gets answer    in <5 seconds    no ticket created]

    B --> H[IAM Identity Center    SSO — employee identity    access control per doc]

    style B fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style H fill:#1a73e8,color:#fff,stroke:#1a73e8
    style G fill:#d4edda,stroke:#28a745
```

Nightly S3 + ServiceNow sync keeps index fresh. Answers include source links so employees can read the full doc.

---

## Q Developer — CI/CD Security Gate

```mermaid
flowchart LR
    A[Engineer pushes code    to GitHub] --> B[CodePipeline triggered]
    B --> C[Q Developer    Security Scan    run_security_scan]
    C --> D{Critical or High    findings?}
    D -->|Yes| E[Pipeline BLOCKED    findings posted to PR]
    D -->|No| F[Pipeline continues    to deploy]
    C --> G[Findings: OWASP Top 10    Hardcoded secrets    CVE dependencies]

    style C fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style E fill:#f8d7da,stroke:#dc3545
    style F fill:#d4edda,stroke:#28a745
```

Engineers also get inline suggestions and chat (`/dev` agent) directly in VS Code — no API setup, just plugin install.

---

## Q in QuickSight — Natural Language BI

```mermaid
flowchart LR
    A[Analyst types:    Show SLA breaches    by category last 30 days] --> B[Q in QuickSight    IT Operations Topic]
    B --> C[NL → SQL translation    using Topic metadata    category / urgency / dates]
    C --> D[QuickSight visual    auto-generated    bar chart + table]
    D --> E[Analyst pins to dashboard    no SQL written]

    style B fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style D fill:#d4edda,stroke:#28a745
```

Topic defines friendly names (`resolution_time` → "Resolution Time"), named entities ("SLA breach"), and aggregation rules so Q understands business context.

---

## Q in Connect — Agent Real-Time Assist

```mermaid
flowchart TD
    A[Customer calls:    I cant access my email    after password reset] --> B[Amazon Connect    Contact Flow]
    B --> C[Q in Connect    Wisdom Assistant    live transcription]
    C -->|semantic search| D[(IT Agent KB    runbooks + resolved tickets)]
    D --> E[Top 3 suggestions    shown to agent    in real time]
    E --> F[Agent selects answer    reads to customer    AHT reduced 35%]

    style C fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style D fill:#FF9900,color:#fff,stroke:#FF9900
    style F fill:#d4edda,stroke:#28a745
```

`get_agent_suggestions()` is called by a Lambda in the Connect Contact Flow, passing the live conversation transcript as the query.

---

## Data Sources per Q Service

```mermaid
flowchart LR
    A[(S3    IT Runbooks    SOPs    Architecture docs)] --> B[Q Business Index]
    C[(ServiceNow    Knowledge articles    Resolved tickets)] --> B
    D[(GitHub    Code repositories)] --> E[Q Developer]
    F[(QuickSight Dataset    IT Tickets DB)] --> G[Q in QuickSight Topic]
    A --> H[Q in Connect KB]
    C --> H
```

---

## Business Impact

| Metric | Before | After |
|--------|--------|-------|
| IT helpdesk tickets/day | 800 | 320 (−60%) |
| Time to find IT answer | 8–12 min | <30 sec |
| Security issues caught pre-prod | 40% | 91% |
| BI report turnaround | 2–3 days | Instant |
| Agent average handle time | 8.2 min | 5.3 min (−35%) |

---

## AWS Services Summary

| Service | API / SDK |
|---------|-----------|
| Q Business | `boto3.client("qbusiness")` |
| Q Developer | `boto3.client("codewhisperer")` + IDE plugin |
| Q in QuickSight | `boto3.client("quicksight")` Topics API |
| Q in Connect | `boto3.client("wisdom")` |
| IAM Identity Center | Employee SSO for Q Business access control |
| Amazon Connect | Contact Flow + Lambda for agent assist |
| S3 | Knowledge base document storage |
| ServiceNow | Data source connector for Q Business |
| CodePipeline | CI/CD integration for Q Developer scans |

---

## Project Structure

```
amazon_q/
├── it_operations_platform.py   # setup + orchestration for all Q services
└── ARCHITECTURE.md
```

---

## IAM Permissions

```json
{
  "Effect": "Allow",
  "Action": [
    "qbusiness:CreateApplication",
    "qbusiness:CreateIndex",
    "qbusiness:CreateDataSource",
    "qbusiness:CreateWebExperience",
    "codewhisperer:CreateCodeScan",
    "codewhisperer:ListCodeScanFindings",
    "quicksight:CreateTopic",
    "wisdom:CreateAssistant",
    "wisdom:CreateKnowledgeBase",
    "wisdom:QueryAssistant",
    "s3:GetObject",
    "s3:PutObject"
  ],
  "Resource": "*"
}
```

---

## Q Apps — Employee-Built No-Code Apps

Q Apps sit on top of Q Business. Employees create form-based apps that use the same
index and identity — no code, no API calls, built in the Q Business console or via API.

### Apps in This Platform

```mermaid
flowchart TD
    A[Q Business Application\nIT-HelpDesk-Assistant\nShared index + identity] --> B[Q Apps Library\nvisible to all employees]

    B --> C[Incident Report Generator\nFill: title, systems, severity, timeline\nQ drafts: full incident report\nin company template]

    B --> D[New Hire Onboarding Checklist\nFill: role, team\nQ generates: personalised checklist\nfrom IT + HR knowledge base]

    B --> E[Change Request Drafter\nFill: change description, risk, rollback\nQ writes: RFC for CAB approval board]

    style A fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style B fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style C fill:#d4edda,stroke:#28a745
    style D fill:#d4edda,stroke:#28a745
    style E fill:#d4edda,stroke:#28a745
```

### How a Q App Works (Incident Report Example)

```mermaid
flowchart LR
    A[Employee opens\nIncident Report Generator] --> B[Fills 4 input cards:\ntitle / systems\nseverity / timeline]
    B --> C[qPlugin card\nsends prompt to Q Business\nwith filled values as context]
    C --> D[Q searches IT runbook index\nfor incident report template\nand past incident examples]
    D --> E[Generated incident report\nSummary / Impact / Root Cause\nTimeline / Action Items]
    E --> F[Employee copies to\nServiceNow / Confluence\nin seconds]

    style C fill:#7b2d8b,color:#fff,stroke:#7b2d8b
    style E fill:#d4edda,stroke:#28a745
```

### Q Apps vs Q Business Chat

| | Q Business Chat | Q Apps |
|--|----------------|--------|
| Interface | Free-form conversation | Structured form + output |
| Best for | Open-ended questions | Repeatable, templated tasks |
| Created by | AWS | Employees (no-code) |
| Shareable | No | Yes — published to library |
| Example | "How do I reset VPN?" | "Generate incident report for this outage" |
