# Performance & Optimization

This prompt defines performance principles and concrete optimizations for startup, memory, UI, network, and storage. It applies to Android projects using DataBinding, RecyclerView, Room, Retrofit, etc. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Performance considerations for new features and refactors. No “premature optimization”; avoid obvious anti-patterns in design.

---

## Startup and perceived performance

- **Startup** — Reduce synchronous work in Application and first screen; defer non-critical init (lazy, WorkManager or background thread); avoid heavy I/O or class loading on the main thread.
- **First screen** — Prioritize data needed for first screen; show skeleton or cached content first, then refresh in background.
- **Perceived** — For slow operations show **loading**, **skeleton**, or progress; use **paging** for lists to avoid loading too much at once.

---

## Memory

- **Leaks** — Avoid Activity/Fragment/ViewModel being held by long-lived objects (static refs, uncancelled coroutines, listeners not removed). Watch lifecycle in DataBinding, callbacks, Rx/Flow subscriptions.
- **Large objects** — For large images use sampling, thumbnails, or a library (e.g. Glide) with cache/reuse. For large lists use **RecyclerView** + ViewHolder reuse; do not nest infinite inflate in ScrollView.
- **Detection** — In dev/internal use **LeakCanary**; use **Android Profiler – Memory** for heap dumps.

---

## UI and lists

- **RecyclerView** — Use **DiffUtil** (or ListAdapter) for incremental updates; avoid notifyDataSetChanged(). Keep item layout flat to reduce measure/layout cost.
- **Overdraw** — Reduce unnecessary backgrounds and overlap; use “Show overdraw” in developer options.
- **ViewStub** — Use **ViewStub** for conditional large blocks to defer inflate and reduce first-screen cost.
- **Main thread** — Do not do heavy compute, I/O, or network on the main thread; use Dispatchers.Default/IO in coroutines and switch to Main only for UI updates.

---

## Network and images

- **Requests** — Set timeouts and retry; merge or request on demand to avoid duplicate calls; use **OkHttp Cache** or app-level cache for cacheable APIs.
- **Images** — Use **Glide** (or project choice) with cache and sampling; for large lists use thumbnail URL or override(width, height); bind to lifecycle to avoid leaks.
- **Connections** — Reuse OkHttpClient; HTTP/2 and connection pooling are handled by the library.

---

## Database (Room)

- **Indexes** — Add @Index for query, sort, JOIN columns; avoid full table scan.
- **Thread** — Run all Room work on background thread or coroutine (Dispatchers.IO); do not call database.query() or dao.insert() on the main thread.
- **Queries** — With **Flow** for reactive queries, respect backpressure and cancellation; for large one-shot results consider **Paging** or paged load.

---

## Build and package

- **ProGuard/R8** — Enable **minify** and **shrinkResources** in release; keep necessary rules (serialization, reflection, native).
- **ABI** — Use **abiFilters** (e.g. arm64-v8a only) to shrink APK if appropriate; prefer **App Bundle** so Play serves device-specific APKs.
- **Resources** — Remove unused resources; use resConfig or dynamic delivery for large assets and locales.

---

## Debug and regression

- **StrictMode** — Enable **StrictMode** in debug (main-thread disk/network) to catch violations early.
- **Profiler** — Use **Android Profiler** CPU, Memory, Network to find hotspots and leaks.
- **Baseline** — Use **Macrobenchmark** / **Baseline Profile** for startup and frame rate regression on key paths.
