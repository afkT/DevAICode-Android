# Background Services

This prompt defines how to implement background work on Android with modern options (WorkManager, Foreground Service, JobScheduler). Scope, rules, and required operations are stated only in this file.

---

## Scope

- Background task execution, long-running services, scheduled work, system events, sync, push handling. Includes Foreground Service, WorkManager, JobScheduler, AlarmManager, BroadcastReceiver.

---

## Service types and when to use

### 1. WorkManager (preferred for deferrable work)

- **Use** — Deferred, periodic, or constraint-based (network, charging, storage).
- **Constraints** — Network, battery not low, storage not low, device idle.
- **Types** — OneTimeWorkRequest, PeriodicWorkRequest, UniqueWork.
- **Chains** — beginWith(), then(); parallel and sequential.

### 2. Foreground Service

- **Use** — Long-running work the user is aware of (playback, navigation, download).
- **Notification** — Must show a notification; API ≥ 29 must declare foreground service type.
- **Lifecycle** — Call **startForeground(notificationId, notification)** soon after start (e.g. within 5s) or the system may ANR or stop the service; when done call stopForeground(), stopSelf().
- **Types** — mediaPlayback, location, dataSync, phoneCall, etc.

### 3. JobScheduler

- **Use** — Scheduled work on API ≥ 21; alternative to legacy AlarmManager.
- **Constraints** — Network, charging, idle, deadline.
- **Behavior** — System may batch or delay for battery.

### 4. BroadcastReceiver

- **Static** — Manifest registration; note API limits (e.g. boot).
- **Dynamic** — Register in code; tie to component lifecycle; unregister to avoid leaks.
- **Implicit** — API ≥ 26 restricts most implicit broadcasts; prefer foreground components or JobScheduler.

---

## Background limits

### 1. App standby

- **Whitelist** — Whitelisted apps can run more freely; user can manage in settings.
- **Restrictions** — Network, job delay, alarms, sync may be limited.
- **Request** — ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS when justified.

### 2. Background activity

- **API ≥ 26** — Starting Activity from background is restricted; use notification instead.
- **API ≥ 28** — Precise location in background restricted; background gets approximate only.
- **API ≥ 30** — Background sensor access restricted; BACKGROUND_SENSOR permission if needed.

---

## Best practices

### 1. Task design

- **Minimal** — Run in background only when necessary; reduce frequency and duration.
- **Batching** — Combine small tasks; reduce wake-ups.
- **Timing** — Use idle, charging, WiFi when possible.
- **User control** — Settings for background behavior.

### 2. Resources

- **Battery** — Avoid frequent wake-ups; use Doze-friendly scheduling.
- **Network** — Prefer WiFi; batch; compress.
- **Memory** — Avoid holding large memory in background; release when done.

### 3. Errors

- **Retry** — Exponential backoff for network; max retries.
- **Fallback** — If system restricts, fall back to foreground and inform user.
- **Logging** — Log task state for debugging and monitoring.

---

## Foreground service implementation

### 1. Notification

- **Need** — Clearly state that a service is running; offer way to stop.
- **Content** — Status, progress, actions.
- **Style** — MediaStyle, BigTextStyle, etc. as appropriate.

### 2. Permission and declaration

- **Manifest** — FOREGROUND_SERVICE.
- **Type** — Declare type (media, location, dataSync, etc.).
- **Runtime** — Location-related foreground may need LOCATION permission.

### 3. Lifecycle

- **Start** — startForegroundService() at the right time.
- **Stop** — stopForeground() and stopSelf() when done.
- **Errors** — Clean up on failure; avoid zombie services.

---

## Quality checklist (self-check or code review)

- [ ] Is the right option chosen for each task (WorkManager vs Foreground vs JobScheduler)?
- [ ] Are background limits respected (especially API ≥ 26)?
- [ ] Does foreground service show notification and declare type?
- [ ] Are constraints set to reduce battery impact?
- [ ] Is service lifecycle correct and leak-free?
- [ ] Are errors and retries handled?
- [ ] Are privacy and permissions considered?
- [ ] Can the user control background behavior where appropriate?
