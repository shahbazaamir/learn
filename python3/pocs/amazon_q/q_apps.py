"""
Q Apps — Employee-Built Mini Apps on top of Q Business
Extension of it_operations_platform.py

Use case: Employees build no-code apps inside Q Business using Q Apps.
Examples in this IT platform:
  - "Incident Report Generator" — fill a form, Q drafts a full incident report
  - "Onboarding Checklist"      — new hire answers questions, Q creates personalised checklist
  - "Change Request Drafter"    — engineer fills fields, Q writes the RFC document

Q Apps sit on top of the Q Business application (same index, same identity).
Employees create and share apps without writing code — via Q Business console or API.
"""

import json
import boto3

qbusiness = boto3.client("qbusiness")
sts       = boto3.client("sts")

ACCOUNT_ID = sts.get_caller_identity()["Account"]


# ── 1. Create "Incident Report Generator" Q App ───────────────────────────────
# Employee fills: incident title, affected systems, severity, timeline
# Q App generates: full incident report in company template format

def create_incident_report_app(app_id: str) -> str:
    resp = qbusiness.create_q_app(
        applicationId=app_id,
        title="Incident Report Generator",
        description="Fill in the details — Q drafts a complete incident report",
        appDefinition={
            "cards": [
                {
                    "textInput": {
                        "id":          "incident-title-card",
                        "title":       "Incident Title",
                        "placeholder": "e.g. Payment service outage",
                        "type":        "TEXT_INPUT",
                    }
                },
                {
                    "textInput": {
                        "id":          "affected-systems-card",
                        "title":       "Affected Systems",
                        "placeholder": "e.g. checkout-api, payments-db",
                        "type":        "TEXT_INPUT",
                    }
                },
                {
                    "textInput": {
                        "id":          "severity-card",
                        "title":       "Severity (P1 / P2 / P3)",
                        "placeholder": "P1",
                        "type":        "TEXT_INPUT",
                    }
                },
                {
                    "textInput": {
                        "id":          "timeline-card",
                        "title":       "Timeline of Events",
                        "placeholder": "14:00 - Alert fired\n14:05 - On-call paged...",
                        "type":        "TEXT_INPUT",
                        "multiLine":   True,
                    }
                },
                {
                    "qPlugin": {
                        "id":     "report-generator-card",
                        "title":  "Generated Incident Report",
                        "prompt": (
                            "Write a formal incident report using this information:\n"
                            "Title: @incident-title-card\n"
                            "Affected Systems: @affected-systems-card\n"
                            "Severity: @severity-card\n"
                            "Timeline: @timeline-card\n\n"
                            "Include sections: Summary, Impact, Root Cause, Timeline, "
                            "Resolution Steps, Action Items. Use company IT runbook format."
                        ),
                        "type": "Q_PLUGIN",
                    }
                },
            ]
        },
    )
    app_arn = resp["appArn"]
    print(f"Q App created: Incident Report Generator — {app_arn}")
    return resp["appId"]


# ── 2. Create "Onboarding Checklist" Q App ────────────────────────────────────
# New hire fills: name, role, team, start date
# Q App generates: personalised onboarding checklist from IT + HR knowledge base

def create_onboarding_app(app_id: str) -> str:
    resp = qbusiness.create_q_app(
        applicationId=app_id,
        title="New Hire Onboarding Checklist",
        description="Get a personalised onboarding checklist for your role and team",
        appDefinition={
            "cards": [
                {
                    "textInput": {
                        "id":    "role-card",
                        "title": "Your Role",
                        "placeholder": "e.g. Software Engineer / Data Analyst / Product Manager",
                        "type":  "TEXT_INPUT",
                    }
                },
                {
                    "textInput": {
                        "id":    "team-card",
                        "title": "Your Team",
                        "placeholder": "e.g. Payments / Data Platform / Customer Success",
                        "type":  "TEXT_INPUT",
                    }
                },
                {
                    "qPlugin": {
                        "id":     "checklist-card",
                        "title":  "Your Personalised Onboarding Checklist",
                        "prompt": (
                            "Create a detailed onboarding checklist for a new @role-card "
                            "joining the @team-card team. "
                            "Include: software to install, access requests, meetings to schedule, "
                            "documentation to read, and first-week goals. "
                            "Use the IT runbooks and HR policies in the knowledge base."
                        ),
                        "type": "Q_PLUGIN",
                    }
                },
            ]
        },
    )
    print(f"Q App created: Onboarding Checklist — {resp['appArn']}")
    return resp["appId"]


# ── 3. Create "Change Request Drafter" Q App ─────────────────────────────────
# Engineer fills: change description, systems affected, rollback plan
# Q App drafts: full RFC / change request document for approval board

def create_change_request_app(app_id: str) -> str:
    resp = qbusiness.create_q_app(
        applicationId=app_id,
        title="Change Request Drafter",
        description="Describe your change — Q writes the RFC for the approval board",
        appDefinition={
            "cards": [
                {
                    "textInput": {
                        "id":    "change-desc-card",
                        "title": "What are you changing?",
                        "placeholder": "e.g. Upgrading PostgreSQL from 14 to 16 on payments-db",
                        "type":  "TEXT_INPUT",
                        "multiLine": True,
                    }
                },
                {
                    "textInput": {
                        "id":    "risk-card",
                        "title": "Risk Level (Low / Medium / High)",
                        "placeholder": "Medium",
                        "type":  "TEXT_INPUT",
                    }
                },
                {
                    "textInput": {
                        "id":    "rollback-card",
                        "title": "Rollback Plan",
                        "placeholder": "e.g. Restore from snapshot taken before upgrade",
                        "type":  "TEXT_INPUT",
                        "multiLine": True,
                    }
                },
                {
                    "qPlugin": {
                        "id":     "rfc-card",
                        "title":  "Generated Change Request Document",
                        "prompt": (
                            "Write a formal Change Request document for the CAB approval board.\n"
                            "Change: @change-desc-card\n"
                            "Risk: @risk-card\n"
                            "Rollback: @rollback-card\n\n"
                            "Include: Change Summary, Business Justification, Technical Details, "
                            "Risk Assessment, Rollback Plan, Testing Evidence, Approval Checklist."
                        ),
                        "type": "Q_PLUGIN",
                    }
                },
            ]
        },
    )
    print(f"Q App created: Change Request Drafter — {resp['appArn']}")
    return resp["appId"]


# ── 4. Publish apps to Q Business library (visible to all employees) ──────────

def publish_app(app_id: str, q_app_id: str):
    qbusiness.update_q_app(
        applicationId=app_id,
        appId=q_app_id,
        appDefinition={},   # no changes to definition
    )
    # Share to all users in the Q Business application
    qbusiness.associate_q_app_user_roles(
        applicationId=app_id,
        appId=q_app_id,
        userRoles=[{"userType": "USER"}],
    )
    print(f"Q App {q_app_id} published to all employees")


if __name__ == "__main__":
    # Requires app_id from it_operations_platform.py setup_q_business()
    app_id = "<q-business-app-id>"

    print("Creating Q Apps...")
    incident_app_id  = create_incident_report_app(app_id)
    onboarding_app_id = create_onboarding_app(app_id)
    change_req_app_id = create_change_request_app(app_id)

    print("\nPublishing apps to employee library...")
    for qapp_id in [incident_app_id, onboarding_app_id, change_req_app_id]:
        publish_app(app_id, qapp_id)

    print("\n✓ Q Apps live in employee portal.")
