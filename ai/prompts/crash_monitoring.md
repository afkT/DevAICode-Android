# Crash Monitoring & Error Reporting

This prompt defines how to integrate and use crash and error reporting in Android apps. It applies to projects using services like Bugly, Firebase Crashlytics, Sentry, or custom reporting. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Crash monitoring integration, non-fatal error reporting, performance monitoring, user behavior tracking, and log collection. Includes Bugly, Firebase Crashlytics, Sentry, and self-hosted systems.

---

## Choosing a service

### 1. Regional (e.g. China)

- **Bugly** — Tencent; good latency in region; Java/Kotlin/Native crash support.
- **UMeng+** — Analytics and crash analysis.
- **Aliyun Mobile Monitoring** — Alibaba cloud mobile monitoring.

### 2. International

- **Firebase Crashlytics** — Google; full-featured; may be restricted in some regions.
- **Sentry** — Open source; multi-language; self-host or cloud.
- **Instabug** — Mobile-focused monitoring and feedback.

### 3. Criteria

- **Audience** — Prefer regional services for regional users, international for global.
- **Features** — Crash analysis, performance, behavior tracking.
- **Cost** — Free tier, paid plans, self-host cost.
- **Integration** — SDK size, setup effort, maintenance.

---

## Integration

### 1. Dependencies

- Add SDK dependency in `build.gradle`.
- Add required **ProGuard keep rules** so critical classes/methods are not obfuscated and reporting still works; confirm SDK rules are merged in release.

### 2. Initialization

- **Application** — Init SDK in `Application.onCreate()`.
- **Environment** — Different config for debug/release; optionally disable or use different project in debug.
- **User ID** — Set user ID, device info, etc. for debugging.

### 3. Customization

- **Callbacks** — Use pre-crash callbacks to attach extra context.
- **Log level** — Configure which levels are collected (verbose/debug/info/warn/error).
- **Sampling** — For high traffic, set sampling to limit volume.

---

## Reporting strategy

### 1. Crashes

- **Auto capture** — SDK captures uncaught and native crashes.
- **Symbolication** — Upload the **mapping file** from each release build to the monitoring platform so stack traces can be symbolicated; otherwise traces are hard to use.
- **Grouping** — Use type, device, version, user for analysis.

### 2. Non-fatal errors

- **Manual** — Use Crashlytics.log(), Bugly.postCatchedException(), etc. for caught exceptions.
- **Business** — Report business errors, network errors, parse errors.
- **Context** — Attach user action, screen state, network state when possible.

### 3. Performance

- **Startup** — Track cold/warm start time.
- **Screens** — Track key screen load time.
- **Jank/ANR** — Detect main-thread blocking and ANR.
- **Memory** — Track usage and leaks.

---

## Best practices

### 1. Privacy and security

- **Filter** — Do not include passwords, national ID, card numbers, etc. in logs or error payloads.
- **Sanitize** — Mask user ID, phone number in reports.
- **Compliance** — Follow GDPR, CCPA, etc.; support user data deletion.

### 2. Performance

- **Async** — Report on a background thread; avoid blocking main thread.
- **Batching** — Batch reports to reduce network calls.
- **Offline** — Cache when offline; send when network is back.

### 3. Debug and test

- **Test** — Verify reporting in dev/staging.
- **Simulate** — Use test button or command to trigger test crashes.
- **Logs** — Provide a way to view local logs for debugging.

---

## Example: initialization

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.DEBUG) {
            Bugly.init(this, "your-app-id", false)
            // or FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
        }
    }
}
```

## Example: manual report

```kotlin
try {
    // business logic
} catch (e: Exception) {
    Bugly.postCatchedException(e)
    // or FirebaseCrashlytics.getInstance().recordException(e)
    showError("Something went wrong, please retry")
}
```

## Example: custom keys

```kotlin
Bugly.setUserId("user_id_123")
Bugly.setTag("vip_user")
Bugly.putUserData("last_page", "home")
Bugly.putUserData("network_type", "wifi")
```

---

## Quality checklist (self-check or code review)

- [ ] Is crash monitoring initialized correctly in release?
- [ ] Are ProGuard rules set so stacks can be symbolicated?
- [ ] Do reports avoid sensitive user data?
- [ ] Do non-fatal reports include enough context?
- [ ] Is monitoring behavior appropriate in debug?
- [ ] Is there a way to test and verify reporting?
- [ ] Are compatibility and migration considered if changing services?
