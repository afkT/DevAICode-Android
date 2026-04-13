# Implementation Phase: Execute and Iterate

The implementation phase is where the approved plan becomes code. With proper planning and annotation, implementation becomes mechanical rather than creative.

## When to Use This Phase

- After plan is approved and annotated
- After todo list is generated
- When all decisions have been made and validated

## Core Principle

**Implementation should be boring. The creative work happened in the annotation cycles.**

Once the plan is right, execution should be straightforward.

## Standard Implementation Prompt

Use this refined prompt across sessions:

```
implement it all. when you're done with a task or phase, mark it as completed in the plan document. do not stop until all tasks and phases are completed. do not add unnecessary comments or KDoc. continuously run lint and compile to make sure you're not introducing new issues.
```

### What This Prompt Encodes

- **"implement it all"**: Execute everything in the plan, don't cherry-pick
- **"mark it as completed in the plan document"**: The plan is the source of truth for progress
- **"do not stop until all tasks and phases are completed"**: Don't pause for confirmation mid-flow
- **"do not add unnecessary comments or KDoc"**: Keep the code clean
- **"continuously run lint and compile"**: Catch problems early, not at the end

### Adapting to Your Stack

The prompt above targets Kotlin/Android. Substitute your project's checks as needed (e.g. `./gradlew lint`, `./gradlew compileDebugKotlin`, unit tests). The principle is the same: validate as you go, not only at the end.

## Why This Works

By the time you say "implement it all," every decision has been made and validated:

- Research provided context
- Plan defined the approach
- Annotation cycle injected your judgment
- Todo list broke it down into tasks

Implementation becomes mechanical execution of a well-defined plan.

## During Implementation

### Your Role Shifts

During implementation, your role shifts from architect to supervisor:

- **Planning phase**: Architect, making big decisions
- **Implementation phase**: Supervisor, providing course corrections

### Short, Terse Corrections

Where a planning note might be a paragraph, an implementation correction is often a single sentence:

```
"You didn't implement the deduplicateByTitle function."
```

```
"You built the settings page in the main app when it should be in the admin app, move it."
```

```
"The API endpoint is wrong, it should be /api/v2/users not /api/users"
```

AI has the full context of the plan and the ongoing session, so terse corrections are enough.

## Frontend Work

Frontend work is the most iterative part. Test in the browser and fire off rapid corrections:

### Visual Adjustments

```
"wider"
```

```
"still cropped"
```

```
"there's a 2px gap"
```

```
"make the text darker"
```

### Using Screenshots

For visual issues, attach screenshots. A screenshot of a misaligned table communicates the problem faster than describing it.

### Referencing Existing Code

Reference existing code constantly:

```
"this table should look exactly like the users table, same header, same pagination, same row density."
```

This is far more precise than describing a design from scratch. Most features in a mature codebase are variations on existing patterns.

## Providing Feedback

### Types of Feedback

#### 1. Missing Implementation

```
"You didn't implement the error handling for the API call"
```

```
"The migration script is missing"
```

#### 2. Wrong Location

```
"This should be in the data layer, not the domain layer"
```

```
"Move this utility function to the shared module"
```

#### 3. Wrong Approach

```
"This should use Flow, not LiveData"
```

```
"Don't create a new repository, use the existing one"
```

#### 4. Style Issues

```
"Follow the existing naming convention"
```

```
"This function is too long, extract it"
```

### Feedback Best Practices

- **Be specific**: Point to exact issues
- **Be concise**: Shorter is better
- **Be direct**: Tell AI what to do
- **Reference existing code**: Show examples

## When Things Go Wrong

### Don't Patch, Revert

When implementation goes in a wrong direction, don't try to patch it. Revert and re-scope:

```
"I reverted everything. Now all I want is to make the list view more minimal — nothing else."
```

### Why Revert Works

Narrowing scope after a revert almost always produces better results than trying to incrementally fix a bad approach.

### When to Revert

- Implementation is fundamentally wrong
- Too many issues to fix individually
- Approach doesn't match requirements
- You realize the plan was flawed

## Staying in the Driver's Seat

Even though you delegate execution to AI, never give it total autonomy over what gets built.

### Why This Matters

AI will sometimes propose solutions that are:
- Technically correct but wrong for the project
- Over-engineered for the use case
- Changing public APIs other parts depend on
- More complex when a simpler option would do

You have context about:
- The broader system
- Product direction
- Engineering culture
- Trade-offs you're willing to make

### How to Stay in Control

#### Cherry-Picking from Proposals

When AI identifies multiple issues, go through them one by one:

```
"for the first one, just use async/awaitAll, don't make it overly complicated; for the third one, extract it into a separate function for readability; ignore the fourth and fifth ones, they're not worth the complexity."
```

#### Trimming Scope

Actively cut nice-to-haves:

```
"remove the download feature from the plan, I don't want to implement this now."
```

This prevents scope creep.

#### Protecting Existing Interfaces

Set hard constraints when you know something shouldn't change:

```
"the signatures of these three functions should not change, the caller should adapt, not the library."
```

#### Overriding Technical Choices

Make specific preferences AI wouldn't know about:

```
"use this model instead of that one"
```

```
"use this library's built-in method instead of writing a custom one"
```

### The Division of Labor

- **AI**: Handles mechanical execution
- **You**: Make the judgment calls

The plan captures the big decisions upfront, and selective guidance handles the smaller ones that emerge during implementation.

## Single Long Sessions

Run research, planning, and implementation in a single long session rather than splitting them across separate sessions.

### Why Single Sessions Work

A single session might:
- Start with deep-reading a folder
- Go through three rounds of plan annotation
- Run the full implementation
- All in one continuous conversation

### Context Window Concerns

You might worry about performance degradation after 50% context window. In practice:

- By the time you say "implement it all," AI has spent the entire session building understanding
- Reading files during research
- Refining mental model during annotation cycles
- Absorbing your domain knowledge corrections

### Auto-Compaction

When the context window fills up, AI's auto-compaction maintains enough context to keep going. The plan document survives compaction in full fidelity.

### Benefits of Single Sessions

- Continuous context and understanding
- No need to re-explain requirements
- AI builds on previous work
- More efficient overall

## Progress Tracking

### Using the Todo List

The todo list in the plan document serves as a progress tracker:

- AI marks items as completed as it goes
- You can glance at the plan at any point
- See exactly where things stand
- Especially valuable in long sessions

### Checking Progress

```
"show me the current status of the todo list"
```

```
"what's left to implement?"
```

```
"are we on track to finish this session?"
```

## Quality Assurance

### Continuous Type Checking

The "continuously run lint and compile" instruction ensures:

- Problems are caught early
- Not at the end when everything is broken
- Easier to fix issues as they arise

### Code Review During Implementation

As AI implements, review:

- Does it follow the plan?
- Is the code clean and maintainable?
- Are there any obvious issues?
- Does it match existing patterns?

### Testing

- Test as features are implemented
- Don't wait until everything is done
- Catch issues early

## Implementation Best Practices

### 1. Let AI Run

Once you say "implement it all," let AI work:

- Don't interrupt constantly
- Let it complete tasks
- Review in batches
- Provide feedback in chunks

### 2. Focus on Big Issues

Don't nitpick every little thing:

- Focus on important problems
- Let AI handle minor details
- Save style issues for code review

### 3. Reference the Plan

Keep referring back to the plan:

- Is AI following the plan?
- Are all tasks being completed?
- Is anything being skipped?

### 4. Trust the Process

If you did the research, planning, and annotation well:

- Implementation should be straightforward
- Trust that the plan is good
- Let AI execute it

## Common Implementation Pitfalls

### 1. Micromanaging

```
"change this variable name"
"add a space here"
"format this differently"
```

Don't do this. Let AI work.

### 2. Not Letting AI Complete

Interrupting AI constantly prevents it from getting into a flow.

### 3. Ignoring the Plan

If AI deviates from the plan, redirect it:

```
"stick to the plan, don't add extra features"
```

### 4. Not Running Type Checks

The "continuously run lint and compile" instruction is there for a reason. Use it.

## Integration with Previous Phases

Implementation builds on all previous work:

1. **Research**: Provided understanding of the system
2. **Planning**: Defined the approach
3. **Annotation**: Injected your judgment
4. **Todo List**: Broke it down into tasks
5. **Implementation**: Executes the plan

## Implementation Checklist

- [ ] Plan is approved and annotated
- [ ] Todo list is generated
- [ ] Standard implementation prompt used
- [ ] AI is marking tasks as completed
- [ ] Type checks are running continuously
- [ ] You're providing terse corrections
- [ ] Progress is being tracked
- [ ] Issues are caught early
- [ ] Code follows the plan
- [ ] All tasks are completed

## Next Steps

After implementation is complete:

1. Review the implemented code
2. Run tests
3. Fix any remaining issues
4. Update documentation if needed
5. Commit the changes

## Example Implementation Flow

```
You: implement it all. when you're done with a task or phase, mark it as completed in the plan document. do not stop until all tasks and phases are completed. do not add unnecessary comments or KDoc. continuously run lint and compile to make sure you're not introducing new issues.

AI: [starts implementing, marking tasks as completed]

[AI completes first phase]

You: [reviews] "You didn't implement the error handling for the API call"

AI: [fixes error handling]

[AI continues implementing]

[AI completes second phase]

You: [reviews] "This should be in the data layer, not the domain layer"

AI: [moves code to data layer]

[AI continues implementing]

[AI completes all tasks]

You: [reviews final implementation] "Looks good, commit it"
```

## Summary

The implementation phase is about executing a well-defined plan. With proper research, planning, and annotation, implementation becomes straightforward. Your role is to supervise, provide course corrections, and ensure quality—not to micromanage every detail.
