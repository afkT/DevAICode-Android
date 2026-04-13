# App Startup & Initialization

This prompt defines how Application and first-screen initialization are ordered, how to defer or lazy-initialize, and how to observe startup. It applies to Android projects using Hilt/Koin, multiple SDKs, and a Splash screen. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Order and cost of initialization in `Application.onCreate`; initialization required by the first screen (Splash / home); use of App Startup or hand-written dependency order. This prompt does not replace per-SDK integration docs; it only defines **when**, **in what order**, and **on which thread** to initialize.

---

## Principles

- **Main thread** — `Application.onCreate` runs on the main thread. Keep **synchronous** work there minimal (e.g. &lt; 500ms) to avoid slowing cold start and ANR risk.
- **Critical path** — Only initialization **required** for first-screen render and interaction should run synchronously in Application (e.g. DI container, crash reporting, essential Context). Non-essential SDKs should be deferred or lazy.
- **Order** — When B depends on A, initialize A first. Independent initializers can run in parallel (e.g. App Startup `Initializer`). Avoid circular dependencies.

---

## Typical initialization in Application

- **Early and synchronous** — Dependency injection (Hilt `@HiltAndroidApp` / Koin `startKoin`), crash reporting (after DI so it can be injected before use), Application Context singleton if the project uses one.
- **Defer or async** — Analytics, push SDK, maps, image library preload, WorkManager enqueue, etc. Post from end of `Application.onCreate` to next frame or `Executors.newSingleThreadExecutor()`, or lazy-initialize on first use.
- **Avoid** — Heavy I/O, network calls, or large class loading in Application; do not start a foreground service or show UI here (Splash is the launcher Activity’s job).

---

## App Startup library (optional)

- **When** — Use **AndroidX App Startup** (`androidx.startup:startup-runtime`) when multiple libraries require “init once” in Application and you want a single place to manage order and dependencies.
- **How** — Implement `Initializer<T>`, run init in `create(context)` and return the instance; register via `InitializationProvider` manifest `meta-data` or call `AppInitializer.getInstance(context).initializeComponent(XxxInitializer::class.java)` manually.
- **Order** — Use `dependencies()` so other Initializers run before this one; avoid cycles.
- **Thread** — `create()` runs on the main thread by default. If init can run on a background thread, use `thread { }` or WorkManager inside the Initializer and respect main-thread-only APIs (e.g. Context).

---

## Splash and first screen

- **Splash** — If using SplashScreen API (Android 12+) or a custom Splash Activity, use it only for branding and waiting for essential init. Do not fetch business data here; the home screen or ViewModel should load data.
- **First-screen dependencies** — If the first screen depends on an SDK (e.g. login SDK), in Splash or home ViewModel check “initialized or not”; show loading or a short Splash until ready, then continue. Or use App Startup lazy init and trigger on first use.
- **Startup time** — Measure the critical path with Macrobenchmark or around `reportFullyDrawn()`; avoid blocking the first screen for non-critical SDKs.

---

## Working with DI

- **Hilt** — With `@HiltAndroidApp`, Hilt is installed during Application `onCreate`. `@Provides` in modules are created on first request (lazy). So in Application do only `super.onCreate()` plus non-Hilt init; get WorkManager, crash reporting, etc. at use sites or via EntryPoint.
- **Koin** — In `Application.onCreate` call `startKoin { androidContext(this); modules(...) }`. `single` in modules are created on first `get()`; make heavy singletons lazy or delay when they are `get()`.

---

## Quality checklist (self-check or code review)

- [ ] Is synchronous work on the main thread in `Application.onCreate` within acceptable time?
- [ ] Is any non-essential init blocking startup?
- [ ] Do multi-SDK init order and dependencies avoid cycles?
- [ ] If using App Startup, are Initializer dependencies and thread usage correct?
- [ ] Does Splash / first screen avoid unnecessary synchronous waiting?
