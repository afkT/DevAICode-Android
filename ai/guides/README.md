# AI-Efficient Development Workflow Guides

Structured workflow for AI-assisted development: research → plan → annotate → implement. Written artifacts (research.md, plan.md) and your annotations keep you in control and reduce rework.

## Overview

Four phases, in order:

| Phase | Purpose | Output |
|-------|---------|--------|
| **[Research](./research-phase.md)** | Deep codebase understanding before any design or code | `research.md` |
| **[Planning](./planning-phase.md)** | Turn research into a concrete, reviewable implementation plan | `plan.md` |
| **[Annotation cycle](./annotation-cycle.md)** | Refine the plan with inline notes; iterate until approved | Updated `plan.md` + todo list |
| **[Implementation](./implementation-phase.md)** | Execute the approved plan; supervise and correct | Working code |

**Core idea:** Do not let AI write code until you have reviewed and approved a **written** research document and a **written** plan. The plan is the contract; annotation is where you inject judgment and constraints.

## Quick Start

1. **[Research](./research-phase.md)** — Deep-read the relevant code; require a written `research.md`. Use emphasis words ("deeply", "in great detail", "intricacies") so AI does not skim.
2. **[Planning](./planning-phase.md)** — Ask for a detailed `plan.md` (approach, code snippets, file list, trade-offs). Base it on the actual codebase.
3. **[Annotation](./annotation-cycle.md)** — Open `plan.md` in your editor; add inline notes; send back to AI with "address all the notes and update the document accordingly. don't implement yet." Repeat until the plan is right (typically 1–6 iterations). Then request a **detailed todo list** in the plan.
4. **[Implementation](./implementation-phase.md)** — Use the standard implementation prompt; AI marks tasks complete in the plan; run lint/compile/tests continuously; give short, direct corrections.

## Guides

### [Research Phase](./research-phase.md)

- **When:** New feature/module, major or architectural changes, or when you need to understand a complex area before changing it.
- **Output:** `research.md` (architecture, data flow, key components, patterns, issues, edge cases).
- **Store under:** `docs/research/` with numbered names (e.g. `001-notification-system.md`).

### [Planning Phase](./planning-phase.md)

- **When:** After research (and before any implementation).
- **Output:** `plan.md` with approach, code snippets, files to modify/create, considerations, and trade-offs.
- **Store under:** `docs/plans/` with numbered names (e.g. `001-cursor-pagination.md`).
- **Exit to annotation:** When the plan has enough detail to review and annotate.

### [Annotation Cycle](./annotation-cycle.md)

- **When:** After AI produces an initial plan; before implementation.
- **What:** You review the plan in your editor, add inline notes (correct assumptions, reject approaches, add constraints), send the doc back to AI with "don't implement yet." Repeat until you approve the plan, then request a **todo list** in the same document.
- **Output:** Approved `plan.md` plus a task breakdown used during implementation.

### [Implementation Phase](./implementation-phase.md)

- **When:** After the plan is approved and the todo list is in the plan.
- **What:** One clear implementation prompt; AI implements and marks tasks complete; you supervise and give terse corrections. Use continuous lint/compile/tests.
- **Output:** Working code that matches the plan.

## File Organization

- **Research:** `docs/research/` — e.g. `001-notification-system.md`, `002-task-scheduling.md`
- **Plans:** `docs/plans/` — e.g. `001-cursor-pagination.md`, `002-notification-system.md`

Numbered files act as a history of design and decisions. Paths are conventions; adapt to your repo.

## Best Practices

- **Emphasis words** in research requests: "deeply", "in great detail", "intricacies" so AI does not skim.
- **Written artifacts only** — require `research.md` and `plan.md`; avoid relying on verbal summaries.
- **Fast iterations** — several short annotation rounds beat one long attempt.
- **You stay in control** — your review and annotations are the gate; never give AI full autonomy over what gets built.
- **Single long sessions** when possible — run research, planning, annotation, and implementation in one session to keep context and avoid re-explaining.

## Common Pitfalls

- **Skipping research** — leads to wrong plans and rework; do not assume you already understand the system.
- **Skipping annotation** — the first plan is rarely right; annotation is the efficient way to fix it before code.
- **Micromanaging implementation** — once you say "implement it all," let AI run and correct in batches; constant interruptions reduce efficiency.
- **Skipping continuous checks** — run lint/compile/tests as you go so issues are caught early.

## Adapting the Workflow

- **Trivial changes:** For one-line or single-file fixes, you may shorten or skip a full research doc; for anything broader, keep research and plan.
- **Other stacks:** Substitute your project’s checks as needed (e.g. `./gradlew lint`, `./gradlew compileDebugKotlin`, unit tests). Same idea: validate continuously.

## Knowledge Base

A condensed reference for all four phases (prompts, checklists, pitfalls) is in **[知识库.md](./知识库.md)**. Use it for quick lookup when framing tasks.

A minimal end-to-end workflow example (simple feature, all steps) is in **[完整工作流程示例.md](./完整工作流程示例.md)**.

## Getting Started

1. Read this overview and the [Research Phase](./research-phase.md).
2. Try the full loop on one small feature: research → plan → annotate → todo list → implement.
3. Adjust to your project (paths, depth of research, how many annotation rounds).
