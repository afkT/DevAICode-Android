# Error Handling & User Feedback

This prompt defines how to represent fallible operations, handle exceptions, and show errors in the UI. It applies to projects using MVVM + Repository. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Return types for operations that can fail (network, DB, file I/O, validation). How ViewModel maps errors to UiState; how Activity/Fragment or DataBinding shows errors.

---

## Return types

### Result / sealed class

- **Result&lt;T&gt;** — For “may fail once” operations, prefer returning `Result<T>` (or Kotlin stdlib Result). In Repository/UseCase catch exceptions and wrap as Result.Failure; do not let raw business exceptions reach the UI.
- **Sealed class** — To distinguish error kinds (e.g. network timeout, auth failure, parse error) use `sealed class Outcome<out T>` or `sealed interface LoadResult` with Success(data), Error(message, code), Loading, etc., for exhaustive when and UI branches.

### Flow

- **Errors in Flow** — Use `catch` to map exceptions to a consumable type (e.g. `emit(Result.failure(e))`), or use try-catch inside `flow { }` and emit error state so the collector always sees a terminal success or failure.

---

## Exception handling

- **Entry checks** — At method start validate parameters (null, format); return early or throw IllegalArgumentException.
- **try-catch** — For known recoverable exceptions (IOException, HttpException) catch and log; decide by layer whether to return Result or rethrow.
- **Logging** — Use project logging or Log; include context (e.g. requestId, userId), no sensitive data; in production reduce or sanitize.
- **Do not swallow** — Avoid empty catch or only printStackTrace(); if the current layer cannot handle, wrap and rethrow or return Result.

---

## User-visible errors

- **Wording** — User-facing messages should be clear and actionable (e.g. “Network error, please check and retry”); avoid raw stack traces or jargon.
- **Retry** — For transient errors (timeout, 5xx) provide retry; encapsulate retry in ViewModel, not in the UI.
- **Fallback** — On list/detail failure show empty state or cached data and “Load failed, retry”.

---

## Network errors

- **Timeout / unreachable** — Configure timeout and optional retry in Retrofit/OkHttp; in Repository map to Result or sealed class.
- **4xx / 5xx** — Parse error body code and message; map to domain error type; UI decides message and whether to redirect (e.g. login).
- **Parse error** — On JSON parse failure return a clear error type so UI can show “Data error”.

---

## Error categories and strategy

- **User error** — Validation, permission; give clear guidance.
- **Network** — Connection, timeout, server error; offer retry and offline support.
- **System** — Storage full, OOM; offer fallback or graceful exit.
- **Business** — Rule violation, state conflict; give concrete guidance.

---

## Monitoring and reporting

- **Crashes** — Integrate Bugly, Crashlytics, or similar; ensure key paths are covered.
- **Non-fatal** — Log and report with context (user ID, device, steps).
- **Metrics** — Track error rate, response time; set alerts.
- **Privacy** — No sensitive data in error logs; sanitize user data.

---

## ViewModel and UiState

- **UiState** — Include `error: String?` or `errorResource: Int?`; keep loading and data separate so UI can show “loading + previous data” or “error + retry”.
- **One-time events** — For errors that should show once (e.g. Toast) use SharedFlow/Channel or project event mechanism so LiveData replay does not trigger duplicate Toast.
- **Clear** — Clear error when user retries or leaves the screen so stale messages do not remain.
