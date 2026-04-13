# Annotation Cycle: Iterative Plan Refinement

The annotation cycle is the most distinctive part of this workflow. It's where you inject your judgment, domain knowledge, and architectural decisions into the plan before any code is written.

## When to Use This Phase

- After AI generates an initial plan
- Before any implementation begins
- When you need to correct assumptions or add constraints
- To align the plan with your product priorities and engineering culture

## Core Principle

**The plan is not good enough until you say it is.**

The annotation cycle transforms a generic implementation plan into one that fits perfectly into your existing system.

## The Annotation Loop

```
AI writes plan.md → You review in editor → Add inline notes → Send AI back → AI updates plan → Satisfied? → Request todo list
```

Repeat this cycle 1 to 6 times as needed.

## How to Add Annotations

### Step 1: Open the Plan in Your Editor

Open the plan.md file in your preferred editor. This gives you:

- Full control over the document
- Ability to think at your own pace
- Precise annotation at the exact location of issues

### Step 2: Add Inline Notes

Add notes directly into the document where issues exist. Notes can:

- Correct assumptions
- Reject approaches
- Add constraints
- Provide domain knowledge AI doesn't have
- Redirect entire sections

### Step 3: Send AI Back

```
I added a few notes to the document, address all the notes and update the document accordingly. don't implement yet
```

The "don't implement yet" guard is essential—without it, AI will jump to code the moment it thinks the plan is good enough.

## Types of Annotations

### 1. Domain Knowledge

Share knowledge AI doesn't have about your tools, conventions, or environment:

```
"use Room auto-migration or manual Migration class, not raw SQL scripts"
```

```
"we use the project logger utility, not android.util.Log directly"
```

```
"all API calls must go through the request interceptor"
```

### 2. Correcting Assumptions

Fix wrong assumptions AI made:

```
"no — this should be a PATCH, not a PUT"
```

```
"this endpoint is already authenticated, don't add auth middleware"
```

```
"the database is PostgreSQL, not MySQL"
```

### 3. Rejecting Approaches

Reject proposed approaches that don't fit:

```
"remove this section entirely, we don't need caching here"
```

```
"don't use websockets, use polling instead"
```

```
"skip the migration script, we'll handle this manually"
```

### 4. Explaining Context

Provide context for why something should change:

```
"the queue consumer already handles retries, so this retry logic is redundant. remove it and just let it fail"
```

```
"this function is called from multiple places, so we can't change its signature"
```

```
"we're deprecating this feature, so don't invest in improving it"
```

### 5. Redirecting Sections

Redirect entire sections of the plan:

```
"this is wrong, the visibility field needs to be on the list itself, not on individual items. when a list is public, all items are public. restructure the schema section accordingly"
```

```
"this entire section should be moved to a separate service, not in the main app"
```

### 6. Adding Constraints

Add constraints AI didn't consider:

```
"not optional" — next to a parameter AI marked as optional
```

```
"must be backward compatible with v1 API"
```

```
"this must work offline"
```

## Annotation Examples

### Short Annotations

Sometimes a note is just two words:

```
"not optional"
```

```
"wrong approach"
```

```
"see above"
```

### Medium Annotations

A sentence or two:

```
"use the existing validator, don't write a new one"
```

```
"this should be a separate module, not inline"
```

### Long Annotations

Sometimes a paragraph explaining a business constraint:

```
"this feature is only available to enterprise customers, so we need to add a license check before allowing access. the license validation logic is in domain/auth/LicenseValidator.kt, use that."
```

Or pasting a code snippet showing the data shape you expect:

```
the response should match this shape:
{
  "id": string,
  "name": string,
  "items": Array<{id: string, quantity: number}>
}
```

## Why This Works So Well

### Shared Mutable State

The markdown file acts as shared mutable state between you and AI:

- You can think at your own pace
- Annotate precisely where something is wrong
- Re-engage without losing context

### Structured Specification

The plan is a structured, complete specification you can review holistically:

- A chat conversation requires scrolling to reconstruct decisions
- The plan captures everything in one place
- Easy to see the big picture

### Injecting Judgment

AI is excellent at:
- Understanding code
- Proposing solutions
- Writing implementations

But AI doesn't know:
- Your product priorities
- Your users' pain points
- The engineering trade-offs you're willing to make

The annotation cycle is how you inject that judgment.

## Iteration Strategy

### Number of Iterations

Typically 1-6 iterations:

- **1-2 iterations**: Minor corrections, small adjustments
- **3-4 iterations**: Moderate changes, some rethinking
- **5-6 iterations**: Major restructuring, significant changes

### When to Stop

Stop when:

- All your concerns are addressed
- The plan aligns with your requirements
- You're confident in the approach
- No more annotations needed

### When to Continue

Continue if:

- AI misunderstood your notes
- New issues emerge during review
- You think of additional constraints
- The approach still doesn't feel right

## Common Annotation Patterns

### Pattern 1: Cherry-Picking

When AI identifies multiple issues, go through them one by one:

```
"for the first one, just use async/awaitAll, don't make it overly complicated; for the third one, extract it into a separate function for readability; ignore the fourth and fifth ones, they're not worth the complexity."
```

### Pattern 2: Trimming Scope

Actively cut nice-to-haves:

```
"remove the download feature from the plan, I don't want to implement this now."
```

### Pattern 3: Protecting Interfaces

Set hard constraints when something shouldn't change:

```
"the signatures of these three functions should not change, the caller should adapt, not the library."
```

### Pattern 4: Overriding Technical Choices

Make specific preferences AI wouldn't know about:

```
"use this model instead of that one"
```

```
"use this library's built-in method instead of writing a custom one"
```

## Best Practices

### 1. Be Specific

- Point to exact locations
- Provide concrete examples
- Explain why something should change

### 2. Be Concise

- Shorter notes are often better
- Get to the point quickly
- Avoid unnecessary explanation

### 3. Be Decisive

- Don't ask AI what it thinks
- Tell it what to do
- Make clear choices

### 4. Iterate Quickly

- Don't try to get it perfect in one pass
- Multiple quick iterations are better
- Learn from each round

## Common Pitfalls

### 1. Vague Annotations

```
"this doesn't seem right" — too vague
```

Better:

```
"this should use the existing cache, not create a new one"
```

### 2. Over-Annotating

- Don't annotate every little thing
- Focus on important issues
- Let AI handle minor details

### 3. Skipping the "Don't Implement Yet"

Always include "don't implement yet" or AI will jump to coding.

### 4. Not Reviewing the Updated Plan

After AI updates the plan, review it again to ensure:
- All notes were addressed
- No new issues were introduced
- The plan still makes sense

## Integration with Planning Phase

The annotation cycle is part of the planning phase:

1. **Planning**: AI generates initial plan
2. **Annotation**: You review and annotate
3. **Iteration**: AI updates based on notes
4. **Repeat**: Until plan is approved
5. **Todo List**: Add detailed task breakdown
6. **Implementation**: Execute the approved plan

## Annotation Checklist

- [ ] Opened plan.md in editor
- [ ] Reviewed entire plan
- [ ] Added inline notes for all concerns
- [ ] Notes are specific and actionable
- [ ] Included "don't implement yet" in feedback
- [ ] Reviewed updated plan after each iteration
- [ ] All concerns addressed
- [ ] Plan aligns with requirements
- [ ] Ready for todo list generation

## Requesting the Todo List

When the plan is approved, request a task breakdown in the same document before implementation. Use a prompt like:

```
add a detailed todo list to the plan, with all the phases and individual tasks necessary to complete the plan - don't implement yet
```

The todo list becomes the progress tracker during implementation. Only after you approve it, proceed to the [Implementation Phase](./implementation-phase.md).

## Next Steps

After completing the annotation cycle:

1. Ensure all your concerns are addressed
2. Request a detailed todo list (prompt above)
3. Review the todo list in the plan
4. Proceed to implementation

## Example Full Cycle

```
You: write a plan.md for adding user search functionality

AI: [writes plan.md with approach, files, code snippets]

You: [opens plan.md, adds notes]
- "use the existing search service, don't create a new one"
- "this should be a GET, not a POST"
- "remove the caching section, we don't need it"
- "the response should include user avatars"

You: I added a few notes to the document, address all the notes and update the document accordingly. don't implement yet

AI: [updates plan.md addressing all notes]

You: [reviews updated plan, adds more notes]
- "the search should be case-insensitive"
- "add pagination support"

You: I added more notes, update the plan accordingly. don't implement yet

AI: [updates plan.md again]

You: [reviews, satisfied]

You: add a detailed todo list to the plan, with all the phases and individual tasks necessary to complete the plan - don't implement yet

AI: [adds todo list to plan.md]

You: implement it all...
```
