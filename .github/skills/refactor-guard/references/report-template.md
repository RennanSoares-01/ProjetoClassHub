# 🛡️ RefactorGuard — Refactoring Assessment

> **Status: ⏸️ Awaiting user approval — no application changes authorized.**
> This is a report template, not an assessment of an actual repository. Replace placeholders only with observed evidence. Use “Not evaluated” or “Unknown” when information is unavailable. Remove this template notice when issuing an actual assessment.

## 1. 📌 Scope and baseline

| Item | Verified information |
|---|---|
| Repository and analyzed modules | `<repository / modules>` |
| Branch, revision, and working-tree state | `<observed values; do not invent>` |
| Java / framework / build versions | `<observed versions and configuration source>` |
| Critical flows and contracts examined | `<flows, requirements, and sources>` |
| Scope actually reviewed | `<files/components reviewed; counts only when measured>` |
| Excluded or inaccessible areas | `<area and reason>` |
| Authorized execution | `<commands, environment, and limits approved by the user>` |
| Baseline confidence and limitations | `<missing tests, unavailable dependencies, incomplete context>` |

## 2. 🧭 Executive summary

**Main maintenance problem:** `<evidence-based description, or not established>`

**Recommended first batch:** `<finding IDs, rationale, and expected qualitative benefit>`

**Current blockers:** `<test failures, unanswered business questions, missing verification>`

| Indicator | Baseline | Evidence / limitation |
|---|---|---|
| Production-code findings | `<verified count or unavailable>` | `<report finding IDs>` |
| Unverified concerns | `<count or unavailable>` | `<questions that require confirmation>` |
| Tests passed | `<reported count or not run>` | `<command/log reference>` |
| Tests failed | `<reported count or not run>` | `<TF IDs / command/log reference>` |
| Tests skipped | `<reported count or unavailable>` | `<reported reasons; not equivalent to passed>` |
| Suites not executed | `<suite names>` | `<reason and resulting uncertainty>` |

Do not turn these indicators into an invented quality score or percentage improvement. Do not combine results from overlapping test executions into misleading totals.

## 3. 🧱 Review by pillar

Use one of: **Reviewed — findings**, **Reviewed — no issue found in scope**, **Requires confirmation**, **Not evaluated**, or **Not applicable, with reason**. None of these is a certification of the entire application.

| Pillar | Review status | Evidence / finding IDs | Remaining question |
|---|---|---|---|
| Business behavior and invariants | `<status>` | `<evidence>` | `<question or none>` |
| Architecture and dependency boundaries | `<status>` | `<evidence>` | `<question or none>` |
| Class and method design | `<status>` | `<evidence>` | `<question or none>` |
| SOLID and simplicity principles | `<status>` | `<evidence>` | `<question or none>` |
| API, DTO, and event contracts | `<status>` | `<evidence>` | `<question or none>` |
| Persistence, transactions, and concurrency | `<status>` | `<evidence>` | `<question or none>` |
| Spring configuration and runtime behavior | `<status>` | `<evidence>` | `<question or none>` |
| External integrations and resilience | `<status>` | `<evidence>` | `<question or none>` |
| Test coverage, scenarios, and assertions | `<status>` | `<evidence>` | `<question or none>` |
| Error handling and failure recovery | `<status>` | `<evidence>` | `<question or none>` |
| Security and sensitive data | `<status>` | `<evidence>` | `<question or none>` |
| Performance and resource management | `<status>` | `<evidence>` | `<question or none>` |
| Observability and operations | `<status>` | `<evidence>` | `<question or none>` |
| Tooling, dependencies, and maintainability history | `<status>` | `<evidence>` | `<question or none>` |

## 4. 🔎 Achados priorizados — leitura rápida

### 📊 Resumo dos apontamentos por criticidade

| Criticidade | Quantidade | Significado |
|---|---:|---|
| 🔴 P0 — Crítica | `<n>` | Evidência de impacto crítico confirmado |
| 🟠 P1 — Alta | `<n>` | Alto impacto respaldado por evidência |
| 🟡 P2 — Média | `<n>` | Melhoria relevante, sem defeito crítico confirmado |
| 🟢 P3 — Baixa | `<n>` | Melhoria pontual |
| ⚪ Pendente | `<n>` | Criticidade não determinada por falta de evidência |

| ID | Criticidade | Classe / arquivo | Problema encontrado | Recomendação | Evidência | Decisão |
|---|---|---|---|---|---|---|
| `<RF-ID>` | `<emoji P0/P1/P2/P3/pendente>` | `<arquivo:linha verificados>` | `<uma frase clara>` | `<ação objetiva>` | `<observado / confirmado / pendente>` | `⏸️ Aguardando` |

**Regras:** lista completa dos achados, ordenada por criticidade; uma linha por ID; frases curtas; sem blocos de texto antes da tabela. Caso necessário, dividir em tabelas por criticidade. A criticidade do problema **não** é o risco de realizar a mudança.

### Detalhes e riscos de implementação (por ID)

### `<RF-ID> — Finding title`

**Evidence status:** `<Observed / User-confirmed / Requires confirmation>`

**Observed evidence:** `<precise code behavior, small relevant excerpt, or command result>`

**Why this matters:** `<specific maintenance difficulty or supported operational/business risk>`

**Proposed adjustment:** `<what changes, why, and why a simpler alternative is insufficient>`

**Files and symbols affected:** `<known scope; unresolved scope is a blocker to editing>`

**Behavior to preserve:** `<rules, outputs, exceptions, side effects, contracts, and ordering that apply>`

**Tests and verification required:** `<existing tests to run; exact proposed additions/changes needing approval>`

**Acceptance criteria:** `<observable result of the improvement; no fabricated numerical benefit>`

**Questions before implementation:** `<only unresolved, decision-relevant questions>`

Duplicate this finding section for each substantive recommendation. Similar issues may share one finding only when their scope and proposed treatment are explicit.

## 5. 🧪 Test failures — user decision required

| Failure ID | Test / phase | Baseline or new? | Expected vs. observed | Evidence | Cause confidence | Decision |
|---|---|---|---|---|---|---|
| `<TF-ID>` | `<test name / setup / compile / runtime>` | `<baseline / post-change / cannot determine>` | `<reported failure; not an invented expectation>` | `<command, exit status, relevant output>` | `<confirmed / unknown>` | `⏸️ Pending user decision` |

For each identified failure, offer these distinct decisions:

| Decision | Meaning |
|---|---|
| **Investigate only** | Diagnose within authorized read/execution scope; do not edit. |
| **Repair the test** | First confirm the correct expectation and approve a specific test patch. |
| **Repair the implementation** | Handle as a separately approved defect fix; do not silently reinterpret it as refactoring. |
| **Accept a temporary exception** | Continue only within the approved batch while leaving the test enabled, unchanged, executed, and visibly failing. |
| **Pause** | Make no further changes. |

**Required question:** “For `<TF IDs>`, should I investigate only, propose a test repair, propose an implementation fix, accept the specified temporary exception, or pause?”

If no test failure was observed, say whether tests actually ran. “Not run” is not “no failures.” A setup failure or missing environment is not automatically a broken test.

## 6. 🛠️ Proposed approval batch

| Batch | Included findings | Authorized file/symbol scope | Proposed production / test edits | Verification commands | Exclusions |
|---|---|---|---|---|---|
| `<B-ID>` | `<RF IDs>` | `<explicit paths and boundaries>` | `<bounded plan, separately identifying test edits>` | `<actual commands proposed for this environment>` | `<items not included>` |

**Implementation sequence:** `<small ordered steps within the batch>`

**Relevant side effects of validation:** `<network access, containers, database writes, generated output, or none verified>`

**Failure handling:** Stop and request a failure-specific decision. Do not fix, disable, hide, or automatically roll back anything without the required approval.

**Rollback proposal:** `<limited reversal of this batch’s changes; never discard unrelated work; requires explicit approval>`

**Open questions blocking this batch:** `<questions or none>`

## 7. ✅ User approval

Natural-language approval is valid when it unambiguously identifies the batch and scope. This form is a convenience, not a requirement:

```text
Approve batch: <B-ID>
Approve findings: <RF IDs>
Approve production edits: <explicit scope from the plan>
Approve passing-test additions/updates: <explicit scope, or none>
Approve execution: <command IDs / exact commands and environment>
Decision for each existing failure: <TF-ID → investigate / specified repair / temporary exception / pause>
Accept verification limitations: <specific limitations, or none>
Do not change: <explicit exclusions>
```

**Closing question:** “Which batch or finding IDs do you approve, and what is your decision for each failing test or verification blocker?”

**⏸️ Stop here. Wait for the user’s response.**
