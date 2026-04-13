# WorkManager & Background Work

This prompt defines how to use WorkManager for deferred, periodic, or constraint-based background work. It applies to projects using Hilt or Koin and coroutines. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Tasks that should run later, on a schedule, or when conditions are met (network, battery, storage): e.g. sync, log upload, cache cleanup, reminders. Does not apply to work that must run immediately or with strict real-time guarantees (use coroutines or Foreground Service for those).

---

## When to use WorkManager

- **WorkManager** — System runs work at an appropriate time; supports constraints (network, charging, storage, etc.); work can be rescheduled after process death. Use for “will eventually run” tasks.
- **Not WorkManager** — For work that must run immediately in background and is independent of screen lifecycle, use `viewModelScope`/ApplicationScope coroutines. For long-running user-visible work use Foreground Service.

---

## Worker and task definition

- **Worker** — Extend **CoroutineWorker** or **Worker**; do work in `doWork()`, return Result.success() / Result.retry() / Result.failure().
- **CoroutineWorker** — Use suspend and coroutines in doWork(); call suspend functions (Repository, Retrofit). Use withContext(Dispatchers.IO) for threading.
- **DI** — With Hilt use **@WorkerInject** or a custom WorkerFactory so Worker constructor parameters are provided by Hilt; in Application set Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build() and pass to WorkManager.initialize().
- **Stateless** — Worker should be stateless; do not access ViewModel or Activity from inside; pass data via InputData or WorkRequest setInputData.

---

## WorkRequest and constraints

- **OneTimeWorkRequest** — One shot; setInitialDelay, addTag, setInputData.
- **PeriodicWorkRequest** — Repeating; minimum interval 15 minutes; consider battery and resource use.
- **Constraints** — Constraints.Builder() for setRequiredNetworkType, setRequiresCharging, setRequiresBatteryNotLow, setRequiresStorageNotLow, etc.; work runs when constraints are satisfied.
- **Unique work** — Use WorkManager.enqueueUniqueWork (REPLACE/KEEP/APPEND) so only one pending instance exists per logical task; avoids duplicate runs.

---

## Enqueue and cancel

- **Enqueue** — Get WorkManager in Application, Repository, or ViewModel and call enqueue(workRequest) or enqueueUniquePeriodicWork. Do not enqueue the same Worker from inside that Worker (except for chained follow-up work).
- **Cancel** — WorkManager.cancelUniqueWork(tag) or cancelWorkById(id); cancel by tag on app exit or logout when appropriate.
- **Observe** — Use WorkManager.getWorkInfosForUniqueWorkLiveData(tag) or getWorkInfoByIdLiveData(id) for ENQUEUED/RUNNING/SUCCEEDED/FAILED/CANCELLED.

---

## Testing

- **Unit** — Test Worker doWork() in isolation; inject Fake Repository and assert calls and return value.
- **Integration** — Use WorkManagerTestInitHelper and TestDriver to control constraints and delay so Worker runs synchronously in tests without real scheduling delay.
