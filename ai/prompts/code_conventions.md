# Code Conventions (Class / Method / Comments / Naming)

This prompt defines format and executable rules for classes, methods, comments, and naming. Scope, rules, and required operations are stated only in this file.

---

## Scope

- All Kotlin/Java source in the project. Applies to Android projects using MVVM, DataBinding, and ViewModel.

---

## Class

### Documentation and order

- **Docs** — All public classes have KDoc (or project JavaDoc); add @author if the project requires it.
- **Member order** (suggested) — Constants → properties → primary/secondary constructors → init → overrides (lifecycle first) → public methods → private methods → inner classes / companion.

### Utility classes

- No instantiation — `private constructor()` or Kotlin `object`.
- Methods — Stateless; parameters read-only where possible; use @JvmStatic for Java interop if needed.

### Naming

- **Package** — Lowercase, dot-separated, matches directory (e.g. com.example.feature.login.ui); consistent within module.
- **Class / interface / object** — UpperCamelCase.
- **View references** — Follow project convention (e.g. mIvXxx, mTvXxx); if none, use binding.xxx or clear names; avoid meaningless abbreviations.

---

## Method

### Documentation

- Public methods — KDoc/JavaDoc with @param and @return (when not Unit); describe non-obvious behavior.

### Implementation

- **Naming** — lowerCamelCase; name should say what it does (e.g. loadUserProfile, submitOrder).
- **Null safety** — At method start, null-check critical parameters; return early or throw IllegalArgumentException.
- **Exceptions** — Use try-catch for expected exceptions and log; do not swallow.
- **Length** — Prefer &lt; 40 lines per method; split into private methods or extensions if longer.
- **Async** — Use suspend and coroutines; for fallible operations return Result&lt;T&gt; or sealed success/failure so callers can handle uniformly.

---

## Comments

- **Public API** — KDoc; keep @param, @return, @throws in sync with behavior.
- **Project** — Add @author at class level if required.
- **Inline** — Short and accurate; explain “why” not “what”; update when code changes.

---

## Alignment with architecture

- **ViewModel** — Expose StateFlow&lt;UiState&gt; or LiveData; keep business logic and state transitions in ViewModel.
- **Repository** — Interface in domain, implementation in data; methods take primitives or value objects, return Flow/suspend.
- **UseCase** — Single responsibility, stateless; callable via invoke; put complex or reused logic in UseCase.

---

## Example request (for AI)

When the user asks to “write class/method per project conventions”, apply this prompt by default. If the user says “no KDoc” or “no method length limit”, follow the user; still prefer the rest of these conventions.
