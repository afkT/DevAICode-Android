# Research Phase: Deep Codebase Understanding

The research phase is the foundation of effective AI-assisted development. Before any planning or implementation, thoroughly understand the relevant parts of the codebase.

## When to Use This Phase

- First time working with a new feature or module
- Major feature development or architectural changes
- When you need to understand complex systems before making changes
- Periodic refresh when codebase has evolved significantly

## When to Shorten or Skip

For **trivial changes** (e.g. a one-line config fix, a single rename), you may shorten research to a quick scan or skip a full research document. For anything that touches architecture, data flow, or multiple files, do not skip—superficial understanding leads to rework.

## Core Principle

**Never let AI write code until you've reviewed and approved a written research document.**

This separation prevents wasted effort, keeps you in control of architectural decisions, and produces significantly better results.

## Deep Reading Directives

Use these prompts to guide AI to thoroughly understand the codebase:

### General Research

```
read this folder in depth, understand how it works deeply, what it does and all its nuances and edge cases. when that's done, write a detailed report of your learnings and findings in research.md
```

### System-Specific Research

```
study the notification system in great detail, understand the intricacies of it and write a detailed research.md document with everything there is to know about how notifications work
```

### Bug Investigation

```
go through the task scheduling flow, understand it deeply and look for potential bugs or incorrect behaviors. the system sometimes runs tasks that should have been cancelled. research the flow thoroughly and document any bugs or suspicious behaviors you find in research.md
```

## Key Emphasis Words

Use these words to signal that surface-level reading is not acceptable:

- "deeply"
- "in great detail"
- "intricacies"
- "go through everything"

Without these words, AI will skim—reading a file, seeing what a function does at the signature level, and moving on.

## The Research Document (research.md)

### Why It Matters

The written artifact is critical. It's not about making AI do homework—it's your review surface. You can:

- Read it to verify AI actually understood the system
- Correct misunderstandings before any planning happens
- Use it as a reference throughout development

If the research is wrong, the plan will be wrong, and the implementation will be wrong. Garbage in, garbage out.

### What Should Be Included

- System architecture and data flow
- Key components and their responsibilities
- Dependencies and relationships
- Existing patterns and conventions
- Potential issues or bugs found
- Edge cases and special behaviors

### Document Organization

Store research documents in a dedicated folder with clear naming:

```
docs/research/
├── 001-notification-system.md
├── 002-task-scheduling.md
├── 003-authentication-flow.md
└── ...
```

Numbered documents serve as a changelog for design thinking, recording the evolution of your understanding.

## Preventing Common Failures

The research phase prevents these expensive failure modes:

### 1. Ignoring Existing Infrastructure

- A function that ignores an existing caching layer
- Not using established error handling patterns
- Bypassing existing validation logic

### 2. Breaking Conventions

- A migration that doesn't account for ORM conventions
- Not following project's architectural patterns
- Inconsistent naming or structure

### 3. Duplicating Logic

- An API endpoint that duplicates logic that exists elsewhere
- Reimplementing functionality already in shared utilities
- Creating parallel implementations instead of reusing

## Maintaining and Updating Research

### Initial Research

- Conduct deep reading before first implementation
- Generate comprehensive research.md
- Review and validate findings

### Iterative Updates

When codebase changes:

```
the codebase has changed since our last research. review the changes and update research.md accordingly. focus on how these changes affect the system we're working with.
```

### Before Major Changes

Before implementing new features:

```
review research.md to refresh your understanding of the system. identify any areas that need updating based on recent changes or new requirements.
```

## Research Checklist

- [ ] Used emphasis words (deeply, intricacies, etc.)
- [ ] Generated research.md document
- [ ] Reviewed and validated findings
- [ ] Identified existing patterns and conventions
- [ ] Found potential issues or bugs
- [ ] Documented edge cases and special behaviors
- [ ] Stored in organized folder structure

## Integration with Development Workflow

The research phase integrates with your overall development process:

1. **Before Planning**: Always research first
2. **Before Implementation**: Refresh research if needed
3. **During Code Review**: Reference research to validate decisions
4. **After Changes**: Update research to reflect new understanding

## Best Practices

- Be specific about what to research (folder, system, feature)
- Require written output, not verbal summaries
- Review research before proceeding to planning
- Keep research documents versioned and organized
- Update research when codebase evolves
- Use research as a reference throughout development

## Common Pitfalls to Avoid

- Skipping research for "simple" changes
- Accepting verbal summaries instead of written documents
- Not reviewing research before planning
- Letting research become outdated
- Researching too broadly instead of focusing on relevant areas

## Next Steps

After completing the research phase:

1. Review and validate the research.md document
2. Proceed to the [Planning Phase](./planning-phase.md)
3. Use research findings to inform the implementation plan
