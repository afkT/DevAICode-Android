# Planning Phase: Detailed Implementation Plan

The planning phase transforms research findings into a concrete, actionable implementation plan. This is where you make architectural decisions and define the approach before any code is written.

## When to Use This Phase

- After completing the research phase
- Before implementing any feature or change
- When you need to validate approach with stakeholders
- For complex features requiring careful design

## Core Principle

**Never let AI write code until you've reviewed and approved a written plan.**

The plan serves as a contract between you and AI, ensuring everyone agrees on the approach before implementation begins.

## Requesting a Detailed Plan

After reviewing research, request a detailed implementation plan in a separate markdown file.

### Feature Implementation Plan

```
I want to build a new feature <name and description> that extends the system to perform <business outcome>. write a detailed plan.md document outlining how to implement this. include code snippets
```

### Refactoring Plan

```
the list endpoint should support cursor-based pagination instead of offset. write a detailed plan.md for how to achieve this. read source files before suggesting changes, base the plan on the actual codebase
```

### Bug Fix Plan

```
we need to fix the task scheduling bug where cancelled tasks still run. write a detailed plan.md explaining the root cause and how to fix it, including code changes and testing strategy.
```

## What the Plan Should Include

### 1. Detailed Explanation

- Clear description of the approach
- Rationale for chosen solution
- Alternative approaches considered and why they were rejected
- Trade-offs and implications

### 2. Code Snippets

- Actual code showing the changes
- Before/after comparisons where helpful
- Key implementation details
- Integration points with existing code

### 3. File Structure

- Complete list of files to be modified
- New files to be created
- Files to be deleted (if any)
- Directory structure changes

### 4. Considerations and Trade-offs

- Performance implications
- Security considerations
- Backward compatibility
- Migration strategy
- Testing requirements

## Using Your Own Markdown Files

### Why Not Built-in Plan Mode

Built-in plan modes often lack flexibility. Your own markdown file provides:

- Full control over content and structure
- Ability to edit in your favorite editor
- Inline notes and annotations
- Persistent artifact in the project
- Version control integration

### File Organization

Store plan documents in a dedicated folder:

```
docs/plans/
├── 001-cursor-pagination.md
├── 002-notification-system.md
├── 003-authentication-refactor.md
└── ...
```

Numbered documents serve as a changelog for design thinking, recording the evolution of your project.

## Reference Implementations

For well-contained features where you've seen a good implementation in an open source repo, share that code as a reference:

```
this is how they do sortable IDs in [project], write a plan.md explaining how we can adopt a similar approach.
```

### Benefits of Reference Implementations

- AI works dramatically better with concrete examples
- Reduces design-from-scratch effort
- Ensures proven patterns are used
- Provides clear expectations

### How to Use References

1. Find a high-quality implementation
2. Extract relevant code snippets
3. Share with AI alongside plan request
4. Ask AI to adapt to your codebase context

## Plan Document Template

```markdown
# Plan: [Feature Name]

## Overview
[Brief description of what this plan achieves]

## Approach
[Detailed explanation of the approach]

## Files to Modify
- [List of files with paths]

## New Files
- [List of new files with paths]

## Implementation Details
[Code snippets and detailed explanations]

## Considerations
- Performance: [implications]
- Security: [considerations]
- Compatibility: [backward/forward]
- Testing: [strategy]

## Trade-offs
[What you're trading off and why]
```

## Integration with Research Phase

The planning phase builds directly on research findings:

1. **Research provides context**: Understanding of existing system
2. **Plan applies context**: How to implement changes within that system
3. **Research validates plan**: Ensure plan respects existing patterns

### Example Flow

```
Research: "The notification system uses a queue-based architecture with retry logic."

Plan: "We'll extend the notification queue to support priority levels by adding a priority field to the queue item model and updating the consumer to sort by priority before processing."
```

## Planning Best Practices

### 1. Be Specific

- Include exact file paths
- Provide concrete code examples
- Specify data structures and interfaces

### 2. Think Through Edge Cases

- What happens on error?
- How does this handle concurrent operations?
- What are the failure modes?

### 3. Consider the Whole System

- How does this affect other components?
- Are there breaking changes?
- What needs to be migrated?

### 4. Plan for Testing

- What tests are needed?
- How to verify correctness?
- What about integration tests?

## Common Planning Pitfalls

### 1. Too High-Level

- Missing implementation details
- Vague descriptions
- No code snippets

### 2. Too Low-Level

- Getting lost in implementation details
- Missing the big picture
- Over-engineering

### 3. Ignoring Constraints

- Not considering existing architecture
- Breaking established patterns
- Ignoring performance requirements

### 4. Skipping Alternatives

- Not considering other approaches
- Missing better solutions
- Not documenting trade-offs

## Reviewing the Plan

Before proceeding to annotation cycle:

1. **Read the plan carefully**
2. **Verify it addresses requirements**
3. **Check for consistency with research**
4. **Identify areas needing clarification**
5. **Prepare your annotations**

**Exit criteria:** The plan is ready for annotation when it includes approach, code snippets, file list, and considerations. After annotation, the plan is "approved" when all your notes are addressed and you are satisfied—then request the todo list and move to implementation.

## Next Steps

After creating the plan:

1. Review the plan document
2. Add inline annotations (see [Annotation Cycle](./annotation-cycle.md))
3. Iterate until plan is approved
4. Proceed to implementation phase

## Planning Checklist

- [ ] Plan includes detailed approach explanation
- [ ] Code snippets show actual changes
- [ ] All files to modify are listed
- [ ] Considerations and trade-offs documented
- [ ] Plan is based on actual codebase (not assumptions)
- [ ] Testing strategy is included
- [ ] Plan is stored in organized folder structure
- [ ] Ready for annotation cycle
