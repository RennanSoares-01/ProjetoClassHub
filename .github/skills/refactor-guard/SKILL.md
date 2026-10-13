---
name: refactor-guard
description: Evidence-based application code quality assessment and controlled refactoring. Use when asked to analyze architecture, SOLID, code smells, Java/Spring quality, tests, or refactoring opportunities, and require explicit user approval before edits.
---

# RefactorGuard

Read `references/agent-policy.md` in full and follow it as the authoritative workflow for this skill. Use `references/report-template.md` to structure the assessment report.

Start in read-only assessment mode. Inspect real repository evidence and distinguish observed facts from unknowns. Do not change application code, tests, dependencies or configuration while assessing. Ask approval before executing any commands with meaningful side effects.

Present the evaluation and a proposed, precisely scoped change batch for explicit user approval. Stop after the report until the user approves. After approval, change only what was approved and verify incrementally. On any failing test, stop modifications and ask the user whether to investigate, fix the test, fix the implementation, temporarily accept the failure, or pause. Never automatically fix, disable, exclude or weaken tests.


## Mandatory report layout — criticality-first summary

The first substantive section of EVERY assessment must be **📊 Resumo dos apontamentos por criticidade**. Present a small count table (P0 critical / P1 high / P2 medium / P3 low / pending evidence), followed immediately by ONE readable Markdown table listing all findings, sorted by severity (P0 → P1 → P2 → P3 → pending), with columns: ID | Criticidade | Arquivo / componente | Problema identificado | Recomendação | Status da evidência | Decisão. Use 🔴 Crítica, 🟠 Alta, 🟡 Média, 🟢 Baixa, ⚪ Pendente as text labels. Keep each cell concise (ideally one sentence) and place code lines in backticks. Do not list findings as long prose before the table. For many findings, subdivide into severity-specific tables with identical columns. Include every finding exactly once.

After summary, present concise numbered details by ID (evidence, impact, change risk, preservation constraints, verification) only where needed. Put unresolved questions, test failures and the bounded approval batch in separate short tables. Never confuse finding priority with risk of implementing the change. No invented priority, test result, source line, measurement, or impact. Findings with insufficient evidence must be Pending evidence, not automatically high or critical. Do not imply tests passed if they were not run. Do not alter code/tests until explicit approval.
