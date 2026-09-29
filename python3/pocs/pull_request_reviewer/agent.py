class PullRequestReviewerAgent:
    def __init__(self):
        self.name = "Pull Request Reviewer"
        self.role = (
            "You are a Pull Request Reviewer. Your task is to review pull requests "
            "and provide feedback on code quality, functionality, and adherence to "
            "best practices."
        )
        self.goals = [
            "Review the code changes in the pull request.",
            "Provide constructive feedback on code quality and functionality.",
            "Ensure that the code adheres to best practices and coding standards.",
            "Identify any potential issues or bugs in the code.",
            "Suggest improvements or alternative approaches if necessary.",
        ]

    def review_pull_request(self, pull_request):
        """
        Review a pull request dictionary and return a structured result.

        Expected input shape:
        {
            "title": "PR title",
            "description": "...",
            "files": [
                {
                    "path": "src/example.py",
                    "content": "...",
                    "changes": 30
                }
            ]
        }
        """
        if pull_request is None:
            raise ValueError("pull_request is required")

        findings = []
        files = pull_request.get("files", [])

        for file in files:
            findings.extend(self._review_file(file))

        if not findings:
            verdict = "approved"
            summary = "No obvious issues found in the provided diff."
        else:
            verdict = "changes_requested"
            summary = self._summarize_findings(findings)

        return {
            "agent": self.name,
            "role": self.role,
            "title": pull_request.get("title", "Untitled PR"),
            "summary": summary,
            "verdict": verdict,
            "findings": findings,
        }

    def _review_file(self, file):
        findings = []
        path = file.get("path", "unknown")
        content = file.get("content", "")
        changes = file.get("changes", 0)

        if not content:
            return findings

        if self._looks_like_sensitive_code(content):
            findings.append(
                {
                    "rule": "sensitive-code-pattern",
                    "severity": "high",
                    "file": path,
                    "message": "Potentially sensitive or unsafe code pattern detected.",
                    "suggestion": "Review secrets, credentials, or unsafe execution patterns before merging.",
                }
            )

        if self._contains_debug_code(content):
            findings.append(
                {
                    "rule": "debug-code",
                    "severity": "medium",
                    "file": path,
                    "message": "Debug statements or temporary code may remain in the change.",
                    "suggestion": "Remove debugging logs, prints, or test stubs before merging.",
                }
            )

        if changes and changes > 300:
            findings.append(
                {
                    "rule": "large-diff",
                    "severity": "medium",
                    "file": path,
                    "message": "This file has a large number of changes, which increases review risk.",
                    "suggestion": "Consider splitting the change into smaller, easier-to-review units.",
                }
            )

        if self._looks_like_missing_tests(path, content):
            findings.append(
                {
                    "rule": "missing-tests",
                    "severity": "medium",
                    "file": path,
                    "message": "The change appears to affect logic without visible test coverage.",
                    "suggestion": "Add or update unit tests covering the modified behavior.",
                }
            )

        return findings

    def _looks_like_sensitive_code(self, content):
        suspicious_patterns = [
            "password",
            "secret",
            "token",
            "api_key",
            "aws_access_key",
            "eval(",
            "exec(",
            "subprocess",
            "os.system",
        ]

        lowered = content.lower()
        return any(pattern in lowered for pattern in suspicious_patterns)

    def _contains_debug_code(self, content):
        debug_patterns = [
            "print(",
            "console.log(",
            "debugger;",
            "TODO:",
            "FIXME:",
        ]
        lowered = content.lower()
        return any(pattern in lowered for pattern in debug_patterns)

    def _looks_like_missing_tests(self, path, content):
        if "test" in path.lower():
            return False

        risky_keywords = [
            "if ",
            "for ",
            "while ",
            "return ",
            "class ",
            "def ",
            "async def ",
        ]
        lowered = content.lower()
        return any(keyword in lowered for keyword in risky_keywords)

    def _summarize_findings(self, findings):
        severities = {"high": 0, "medium": 0, "low": 0}
        for finding in findings:
            severity = finding.get("severity", "low").lower()
            severities[severity] = severities.get(severity, 0) + 1

        parts = []
        for level, count in severities.items():
            if count:
                parts.append(f"{count} {level}")

        severity_summary = ", ".join(parts) if parts else "none"
        return (
            f"The pull request has review concerns: {severity_summary}. "
            "Please inspect the flagged areas and address the highest-risk items before merging."
        )
