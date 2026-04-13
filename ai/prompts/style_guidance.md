# Style Guidance (Code Style and General Practice)

This prompt defines project code style and general practices: principles and tradeoffs. Scope, rules, and required operations are stated only in this file.

---

## Scope

- All Kotlin app and utility code. Applies to Android projects using MVVM, DataBinding, and ViewModel.

---

## Kotlin style

- **Idioms** — Prefer data class, sealed class, exhaustive when, extension functions (in moderation), scope functions (apply, also, let) when they improve readability.
- **Null safety** — Use nullable types and ?., ?:; use !! only with good reason and a comment; avoid unnecessary Optional-style wrappers.
- **Extensions** — Use for readability or reuse (UI, collections, strings); avoid over-abstraction or tight coupling to business.
- **Immutability** — Prefer val; when mutable state is needed, keep it scoped (e.g. MutableStateFlow in ViewModel, expose StateFlow).

---

## Code quality

- **Readability** — Code as documentation; names and structure should make intent clear; add short comment or KDoc for complex logic.
- **Consistency** — Naming, package layout, file organization match the project; new code follows data/domain/ui/di structure.
- **Simplicity** — Remove redundancy when it does not hurt readability; avoid abstraction or DSL that does not pay off.

---

## Error handling and robustness

- **Null** — At method entry null-check critical parameters; return early or throw with a clear message.
- **Exceptions** — Catch recoverable exceptions and log (project logger or Log); do not swallow or only printStackTrace().
- **Fallback** — On network/storage failure show a clear message or default data; avoid blank screen or crash.
- **Main thread** — Do not do heavy I/O, network, or compute on the main thread; use Dispatchers.IO/Default in coroutines and Main only for UI.
- **Cleanup** — Cancel listeners, subscriptions, coroutines when appropriate so Activity/Fragment/ViewModel are not held and leaked.

---

## Abstraction and layers

- **Minimal indirection** — Do not add a layer “in case we switch later”; add when it clearly helps testability, reuse, or readability.
- **SOLID** — Apply when it improves maintainability or testability; do not force interfaces and implementations for trivial cases.

---

## Utilities and static behavior

- **Utility** — Private constructor + object methods (or top-level functions); for Java interop use private constructor() + @JvmStatic.
- **Parameters** — Prefer read-only (val/final); avoid mutating passed references inside the method.

---

## Logging

- **Implementation** — Use project logging if present, otherwise android.util.Log; use d/i/w/e by level.
- **Production** — Do not log sensitive data or high-volume debug in release; control via BuildConfig or logging implementation.

---

## Documentation

- **Complex logic** — Algorithms, state machines, cross-module contracts need a short comment or KDoc.
- **Decisions** — Important choices (e.g. why StateFlow here instead of LiveData) in code or design doc so future changes are easier.
