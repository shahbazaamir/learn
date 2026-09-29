

# Architecture of Pull Request Reviewer

## 1. Overview
The Pull Request Reviewer is an automated system designed to evaluate incoming pull requests, inspect code changes, and provide actionable review feedback. It focuses on identifying risk, quality issues, test coverage gaps, and potential security concerns before a PR is merged.

The system is structured as a layered architecture to separate:
- external integrations,
- domain models and rules,
- analysis services,
- and output/reporting.

## 2. Goals
- Review pull requests from a Git hosting provider.
- Parse and understand code changes across multiple files.
- Detect common quality, maintainability, and security risks.
- Estimate the impact of a change on tests and application behavior.
- Produce clear reviewer-friendly summaries and recommendations.
- Integrate cleanly into CI or review workflows.

## 3. High-Level Architecture

### 3.1 Input Layer
This layer is responsible for retrieving PR metadata and code changes from a source such as GitHub or GitLab.

Responsibilities:
- Authenticate with the provider
- Fetch PR metadata, files, comments, labels, and status
- Retrieve diff data for base and head revisions
- Normalize provider-specific responses into internal models

Key components:
- Provider client
- Repository connector
- Event listener / webhook handler

### 3.2 Analysis Layer
This layer inspects diffs and applies logic to infer issues and risks.

Responsibilities:
- Parse changed files and diffs
- Classify changes by language and risk type
- Evaluate rules and heuristics
- Detect security, quality, and test impact issues
- Aggregate findings into review signals

Key components:
- Diff parser
- File analyzer
- Rule engine
- Security analyzer
- Test impact analyzer
- Risk scoring engine

### 3.3 Decision Layer
This layer transforms raw findings into a final review decision.

Responsibilities:
- Aggregate findings into an overall score
- Rate severity and confidence
- Decide whether a PR should be approved, flagged, or blocked
- Summarize findings into human-readable output

Key components:
- Review decision engine
- Recommendation generator
- Summary builder

### 3.4 Output Layer
This layer converts analysis results into an artifact that can be consumed by humans or systems.

Responsibilities:
- Format comments for the Git provider
- Emit status checks or review summaries
- Produce logs and metrics
- Support CLI or API output for local use

Key components:
- PR comment formatter
- Status reporter
- Metrics exporter
- CLI/API interface

## 4. Main Data Flow
1. A pull request event is received from the Git provider.
2. The provider adapter authenticates and fetches PR metadata and diff data.
3. The diff parser converts patch content into structured file changes.
4. The analysis layer identifies relevant patterns, risks, or missing validation.
5. The scoring engine combines findings into a review summary.
6. The output layer publishes comments or statuses back to the provider.

## 5. Core Domain Models
The project should model domain objects explicitly rather than passing raw payloads around.

### PullRequest
Represents the PR being reviewed:
- id
- repository
- base branch
- head branch
- author
- status
- changed files
- reviewers

### FileChange
Represents a single file change:
- path
- file type / language
- added lines
- removed lines
- diff hunks
- associated findings

### Finding
Represents a rule violation or signal:
- rule id
- title
- severity
- confidence
- description
- file path
- line numbers
- suggested fix

### ReviewResult
Represents the final review outcome:
- overall score
- summary
- findings
- recommendations
- approval status

## 6. Proposed Component Structure
```text
pull_request_reviewer/
├── app/
│   ├── cli.py
│   ├── api.py
│   └── workers.py
├── adapters/
│   ├── github_client.py
│   ├── gitlab_client.py
│   └── provider_factory.py
├── domain/
│   ├── models.py
│   ├── findings.py
│   ├── review_result.py
│   └── policies.py
├── services/
│   ├── pull_request_service.py
│   ├── diff_service.py
│   ├── risk_service.py
│   ├── coverage_service.py
│   └── report_service.py
├── rules/
│   ├── security_rules.py
│   ├── quality_rules.py
│   └── style_rules.py
├── infrastructure/
│   ├── config.py
│   ├── logging.py
│   └── metrics.py
├── tests/
│   ├── test_diff_service.py
│   ├── test_risk_service.py
│   └── test_review_result.py
├── main.py
└── README.md
```

## 7. Responsibility Boundaries
### Provider Adapters
- Handle platform differences
- Fetch raw data
- Return normalized domain data

### Diff and File Services
- Parse patch structure
- Identify touched modules and affected files
- Estimate change complexity

### Rule Engine
- Evaluate patterns
- Return structured findings with severity/confidence
- Avoid mixing rule logic with transport code

### Risk Engine
- Combine all findings
- Weight severity, change volume, and testing context
- Generate a final review verdict

### Reporting
- Convert verdicts into human-readable output
- Post comments or status updates
- Keep output concise and actionable

## 8. Typical Review Workflow
```text
PR Webhook/Event
    ↓
Provider Adapter
    ↓
Pull Request Service
    ↓
Diff Service
    ↓
Rule Engine + Security Rules + Coverage Checks
    ↓
Risk Scoring
    ↓
ReviewResult
    ↓
Comment/Status Output
```

## 9. Design Principles
- Separate provider logic from core review logic.
- Prefer explicit domain models over ad hoc dictionaries.
- Keep scoring transparent and explainable.
- Make findings actionable with suggested fixes.
- Use small, testable services rather than a monolithic analyzer.
- Support pluggable rules for different project types or policy requirements.

## 10. Quality Criteria
The architecture should support:
- Fast PR evaluation
- Deterministic scoring
- Clear, explainable findings
- Safe handling of sensitive code or secrets
- Reliable behavior under temporary provider or network issues

## 11. Extension Points
The design should allow future additions such as:
- new Git providers
- custom policy packs
- additional linting or security integrations
- code ownership and reviewer metadata
- support for multiple languages and frameworks
- custom dashboards or analytics

## 12. Risks and Trade-offs
- Heuristic-based review can produce false positives.
- Large diffs can dilute signal quality.
- Security and lint rules may require external tools or language-specific parsers.
- Overly broad scoring rules may reduce trust among reviewers.
- Provider rate limits and API instability must be handled gracefully.

## 13. Summary
The Pull Request Reviewer architecture follows a layered, modular design. The provider layer fetches PR data, the analysis layer inspects diffs and applies rules, and the decision/reporting layer turns those findings into actionable reviewer feedback. This structure keeps the system understandable, extensible, and suitable for production use in collaborative code review environments.