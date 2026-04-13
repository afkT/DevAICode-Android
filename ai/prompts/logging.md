# Logging & Debug

This prompt defines how to log in the app, log levels, and Debug vs Release behavior. It applies to all Kotlin/Java app and library code. Scope, rules, and required operations are stated only in this file.

---

## Scope

- How to log from app and base libraries; how Debug and Release differ; no sensitive data in logs; what to attach to crash/reporting. This prompt does not define a specific logging library API; it defines a single entry point and level semantics.

---

## Single entry point and levels

- **Entry** — Use a single logging facade (e.g. **Timber** or project `LogUtil`). Do not call `android.util.Log` directly from app code; this allows global control of level, tag rules, and turning off logs in release.
- **Levels** — **VERBOSE/DEBUG**: development only. **INFO**: important flow milestones (e.g. “request start/end”). **WARN**: recoverable issues or fallbacks. **ERROR**: issues that need attention. In production, keep only WARN/ERROR or sampled INFO.
- **Tag** — Use a tag that matches class or module (e.g. `Timber.tag("LoginViewModel")` or a module prefix). Avoid one tag for the whole app.

---

## Debug vs Release

- **BuildConfig.DEBUG** — Use `BuildConfig.DEBUG` or a custom `BuildConfig.LOGGING_ENABLED` to control DEBUG/VERBOSE in release. Do not emit verbose debug logs in release (performance and information leakage).
- **Implementation** — In Application or log init: in release use `Timber.plant(ReleaseTree())` (no log or report-only); in debug use `Timber.plant(DebugTree())`. Trees can add tag prefix, line number, etc.
- **Third-party** — For OkHttp LoggingInterceptor, Retrofit logs, etc., enable only in debug builds and do not log sensitive fields in request/response body (or sanitize).

---

## Sensitive data

- **Never log** — Passwords, payment secrets, full tokens, national ID, card numbers, full phone numbers (masked is OK, e.g. 138****1234). Do not include these in logs or crash attachments.
- **Sanitize** — If you must log user-related identifiers, mask them (e.g. last digits of userId, partial order id). Align with security rules; in release, ensure the facade never emits sensitive content.
- **Crash / reporting** — Do not put sensitive data in custom key-value or breadcrumbs; use page path, step, non-sensitive state to help reproduction.

---

## Required actions (this file only)

- **Init** — In `Application.onCreate` (or before first log), configure the logging library `plant` and choose DebugTree/ReleaseTree from BuildConfig.
- **App code** — Log only through the single entry with appropriate level and tag. In catch blocks use `log.warn(e, "message")` or equivalent; do not swallow exceptions or only `printStackTrace()`.
- **Code review** — For new logs, check for sensitive data and that release does not log excessively; confirm third-party logging is enabled only in debug.

---

## Quality checklist (self-check or code review)

- [ ] Is a single logging entry used (Timber or project LogUtil), with no scattered `Log.d` / `println`?
- [ ] Are DEBUG/VERBOSE disabled or heavily limited in release?
- [ ] Do logs and crash attachments exclude passwords, tokens, national ID, card numbers, full phone numbers?
- [ ] Is third-party network/library logging enabled only in debug and sensitive fields sanitized?
