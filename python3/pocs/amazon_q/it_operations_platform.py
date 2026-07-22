"""
Enterprise IT Operations Platform — Amazon Q (All Services)
Production use case: A 10,000-employee enterprise deploys all Amazon Q surfaces
to reduce IT helpdesk tickets, accelerate developer productivity, enable
self-service BI, and improve customer support agent efficiency.

Amazon Q Services used:
  1. Q Business       — Employee IT self-service chatbot (internal knowledge base)
  2. Q Developer      — Engineering team code reviews, security scans, upgrades
  3. Q in QuickSight  — BI analysts ask natural language questions on dashboards
  4. Q in Connect     — Customer support agents get real-time answer suggestions

This file orchestrates the AWS-side setup for all four via boto3.
"""

import json
import boto3

qbusiness  = boto3.client("qbusiness")
qdeveloper = boto3.client("codewhisperer")   # Q Developer API surface
quicksight = boto3.client("quicksight")
connect    = boto3.client("connect")
s3         = boto3.client("s3")
iam        = boto3.client("iam")

ACCOUNT_ID    = boto3.client("sts").get_caller_identity()["Account"]
REGION        = "us-east-1"
KB_BUCKET     = f"it-knowledge-base-{ACCOUNT_ID}"   # S3 bucket with IT docs


# ══════════════════════════════════════════════════════════════════════════════
# 1. Q BUSINESS — Employee IT Self-Service Chatbot
#    Indexes: IT runbooks, HR policies, software docs, Confluence, ServiceNow
#    Employees ask: "How do I reset my VPN?" "What's the PTO policy?"
# ══════════════════════════════════════════════════════════════════════════════

def setup_q_business():
    # Create Q Business application
    app = qbusiness.create_application(
        displayName="IT-HelpDesk-Assistant",
        description="Employee self-service IT helpdesk powered by Amazon Q",
        roleArn=f"arn:aws:iam::{ACCOUNT_ID}:role/QBusinessServiceRole",
        identityCenterInstanceArn=f"arn:aws:sso:::instance/ssoins-<id>",  # IAM Identity Center
        attachmentsConfiguration={"attachmentsControlMode": "ENABLED"},
    )
    app_id = app["applicationId"]

    # Create index to store document embeddings
    index = qbusiness.create_index(
        applicationId=app_id,
        displayName="IT-Knowledge-Index",
        type="ENTERPRISE",
        capacityConfiguration={"units": 1},
    )
    index_id = index["indexId"]

    # Data source 1: S3 — IT runbooks, SOPs, architecture docs
    qbusiness.create_data_source(
        applicationId=app_id,
        indexId=index_id,
        displayName="IT-Runbooks-S3",
        roleArn=f"arn:aws:iam::{ACCOUNT_ID}:role/QBusinessDataSourceRole",
        configuration={
            "type": "S3",
            "connectionConfiguration": {"repositoryEndpointMetadata": {"BucketName": KB_BUCKET}},
            "repositoryConfigurations": {
                "document": {
                    "fieldMappings": [
                        {"dataSourceFieldName": "s3_document_id", "indexFieldName": "id",    "indexFieldType": "STRING"},
                        {"dataSourceFieldName": "s3_document_modified_date", "indexFieldName": "updated_at", "indexFieldType": "DATE"},
                    ]
                }
            },
            "syncMode": "FULL_CRAWL",
        },
        syncSchedule="cron(0 2 * * ? *)",   # nightly sync
    )

    # Data source 2: ServiceNow — resolved tickets become searchable knowledge
    qbusiness.create_data_source(
        applicationId=app_id,
        indexId=index_id,
        displayName="ServiceNow-Resolved-Tickets",
        roleArn=f"arn:aws:iam::{ACCOUNT_ID}:role/QBusinessDataSourceRole",
        configuration={
            "type": "SERVICENOW",
            "connectionConfiguration": {
                "repositoryEndpointMetadata": {
                    "hostUrl":        "https://<company>.service-now.com",
                    "serviceCatalog": "KNOWLEDGE",
                }
            },
            "syncMode": "FULL_CRAWL",
        },
    )

    # Web experience — embeddable chat UI for employee portal
    web = qbusiness.create_web_experience(
        applicationId=app_id,
        title="IT Help Assistant",
        subtitle="Ask me anything about IT, HR policies, or software",
        welcomeMessage="Hi! I can help with VPN, software access, PTO, and more.",
        roleArn=f"arn:aws:iam::{ACCOUNT_ID}:role/QBusinessWebExperienceRole",
    )

    print(f"Q Business app: {app_id}")
    print(f"Web experience URL: {web['defaultEndpoint']}")
    return app_id, index_id


# ══════════════════════════════════════════════════════════════════════════════
# 2. Q DEVELOPER — Engineering Productivity
#    - Code reviews in IDE (VS Code / JetBrains plugin — no API setup needed)
#    - Security scans via CodeWhisperer Security API
#    - Automated dependency upgrades via Q Developer Agent
# ══════════════════════════════════════════════════════════════════════════════

def run_security_scan(code_snippet: str, language: str = "python") -> dict:
    """
    Q Developer security scan — detects OWASP Top 10, secrets, CVEs in code.
    Called in CI/CD pipeline (CodePipeline) before merge to main.
    """
    resp = qdeveloper.create_code_scan(
        artifacts=[
            {
                "sourceCodeType": {
                    "repositoryAnalysis": {
                        "repositoryHead": {"branchName": "main"},
                    }
                }
            }
        ],
        programmingLanguage={"languageName": language},
        clientToken=f"scan-{hash(code_snippet)}",
    )
    scan_id = resp["jobId"]
    print(f"Q Developer security scan started: {scan_id}")
    return {"scan_id": scan_id, "status": resp["status"]}


def get_scan_findings(scan_id: str) -> list[dict]:
    """Poll scan results — integrated into CodePipeline approval gate."""
    resp     = qdeveloper.list_code_scan_findings(
        jobId=scan_id,
        codeAnalysisFindingsSchema="codescan/findings/1.0",
    )
    findings = resp.get("codeScanFindings", [])
    for f in findings:
        print(f"[{f['severity']}] {f['title']} — {f['filePath']}:{f['startLine']}")
    return findings


# ══════════════════════════════════════════════════════════════════════════════
# 3. Q IN QUICKSIGHT — Natural Language BI
#    BI analysts ask: "Show me ticket volume by category last 30 days"
#    Q generates the visual automatically — no SQL needed
#    Setup: enable Q in QuickSight console + create Topics linked to datasets
# ══════════════════════════════════════════════════════════════════════════════

def setup_quicksight_q_topic():
    """
    Create a Q Topic linked to the IT operations dataset.
    Analysts can then ask natural language questions in QuickSight.
    """
    quicksight.create_topic(
        AwsAccountId=ACCOUNT_ID,
        TopicId="it-operations-topic",
        Topic={
            "Name": "IT Operations Analytics",
            "Description": "Ticket volumes, resolution times, agent performance",
            "DataSets": [
                {
                    "DatasetArn": f"arn:aws:quicksight:{REGION}:{ACCOUNT_ID}:dataset/it-tickets-dataset",
                    "DatasetName": "IT Tickets",
                    "DatasetDescription": "Support tickets with category, urgency, resolution time",
                    "DataAggregations": [
                        {"DatasetSchema": {"ColumnSchemaList": []}, "GroupByColumns": ["category", "urgency", "agent_id"]},
                    ],
                    "Filters": [],
                    "Columns": [
                        {"ColumnName": "ticket_id",       "ColumnFriendlyName": "Ticket ID",        "ColumnDataRole": "DIMENSION"},
                        {"ColumnName": "category",        "ColumnFriendlyName": "Category",         "ColumnDataRole": "DIMENSION"},
                        {"ColumnName": "urgency",         "ColumnFriendlyName": "Urgency",          "ColumnDataRole": "DIMENSION"},
                        {"ColumnName": "resolution_time", "ColumnFriendlyName": "Resolution Time",  "ColumnDataRole": "MEASURE"},
                        {"ColumnName": "created_at",      "ColumnFriendlyName": "Created Date",     "ColumnDataRole": "DIMENSION"},
                    ],
                    "NamedEntities": [
                        {"EntityName": "SLA breach", "EntityDescription": "Tickets not resolved within SLA window"},
                    ],
                }
            ],
        },
    )
    print("QuickSight Q Topic created: it-operations-topic")
    print("Analysts can now ask: 'How many High urgency tickets breached SLA last week?'")


# ══════════════════════════════════════════════════════════════════════════════
# 4. Q IN CONNECT — Customer Support Agent Assist
#    When a customer calls/chats, Q in Connect listens to the conversation
#    and surfaces real-time answer suggestions to the agent from the KB.
#    Reduces average handle time (AHT) by 20–40%.
# ══════════════════════════════════════════════════════════════════════════════

def setup_q_in_connect():
    """
    Create Amazon Q in Connect assistant + knowledge base.
    Associate with Amazon Connect instance for real-time agent suggestions.
    """
    wisdom = boto3.client("wisdom")

    # Create assistant
    assistant = wisdom.create_assistant(
        name="IT-Support-Agent-Assistant",
        type="AGENT",
        description="Real-time answer suggestions for IT support agents",
        serverSideEncryptionConfiguration={
            "kmsKeyId": f"arn:aws:kms:{REGION}:{ACCOUNT_ID}:key/<key-id>"
        },
    )
    assistant_id = assistant["assistant"]["assistantId"]

    # Create knowledge base linked to S3 IT docs
    kb = wisdom.create_knowledge_base(
        name="IT-Agent-KB",
        knowledgeBaseType="EXTERNAL",
        description="IT runbooks and resolved ticket solutions for agent suggestions",
        sourceConfiguration={
            "appIntegrations": {
                "appIntegrationArn": f"arn:aws:app-integrations:{REGION}:{ACCOUNT_ID}:data-integration/it-docs",
                "objectFields": ["title", "body", "category"],
            }
        },
    )
    kb_id = kb["knowledgeBase"]["knowledgeBaseId"]

    # Associate KB with assistant
    wisdom.create_assistant_association(
        assistantId=assistant_id,
        association={"knowledgeBaseId": kb_id},
        associationType="KNOWLEDGE_BASE",
    )

    print(f"Q in Connect assistant: {assistant_id}")
    print("Agents will now see real-time suggestions during customer conversations")
    return assistant_id, kb_id


def get_agent_suggestions(assistant_id: str, session_id: str, query: str) -> list[dict]:
    """
    Called during live customer interaction — returns top answer suggestions.
    Integrated into Connect Contact Flow via Lambda.
    """
    wisdom = boto3.client("wisdom")
    resp   = wisdom.query_assistant(
        assistantId=assistant_id,
        sessionId=session_id,
        queryText=query,
    )
    return [
        {
            "title":     r["document"]["title"]["text"],
            "excerpt":   r["document"]["excerpt"]["text"],
            "relevance": r["relevanceScore"],
        }
        for r in resp.get("results", [])
    ]


# ── Orchestrate full setup ────────────────────────────────────────────────────

if __name__ == "__main__":
    print("=" * 60)
    print("1. Setting up Q Business (Employee IT Helpdesk)...")
    app_id, index_id = setup_q_business()

    print("\n2. Q Developer — security scans run automatically in CI/CD")
    print("   Install Q Developer plugin in VS Code / JetBrains for inline suggestions")

    print("\n3. Setting up Q in QuickSight (BI Natural Language)...")
    setup_quicksight_q_topic()

    print("\n4. Setting up Q in Connect (Agent Assist)...")
    assistant_id, kb_id = setup_q_in_connect()

    print("\n✓ All Amazon Q services configured.")
