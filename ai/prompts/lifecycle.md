# Lifecycle (Lifecycle and Coroutine Scope)

This prompt defines how to start coroutines from Activity/Fragment based on Lifecycle and how to observe lifecycle state. It applies to projects using ViewModel and coroutines. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Coroutines that should run “when UI is visible” and stop when not (e.g. polling, subscription, location updates).
- Using a coroutine scope outside ViewModel (e.g. one-off request in Fragment/Activity).
- Using LifecycleObserver to run logic at lifecycle events.

---

## lifecycleScope

- **Purpose** — In Activity/Fragment use **lifecycleScope** to launch coroutines; they are cancelled when the Activity/Fragment is destroyed, avoiding leaks and background work.
- **Usage** — lifecycleScope.launch { ... } or lifecycleScope.launchWhenStarted { ... }. In **Fragment** prefer **viewLifecycleOwner.lifecycleScope** so work does not continue after Fragment is detached.
- **When** — One-off work (button-triggered request, collect Flow to update UI) can use viewLifecycleOwner.lifecycleScope.launch. If data is not tied to this screen or must survive config change, use ViewModel **viewModelScope** instead.

---

## repeatOnLifecycle

- **Purpose** — Run a block (e.g. collect Flow) only while the lifecycle is **at least** in a given state; when the lifecycle drops below that state the block is cancelled; when it enters again the block runs again.
- **API** — lifecycleScope.launch { lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect { ... } } }. Often **Lifecycle.State.STARTED** (visible, from onStart to before onStop) or RESUMED (foreground).
- **Fragment** — Use **viewLifecycleOwner**: viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { ... } } so that collection does not continue after the Fragment is detached.
- **Alternative** — Do not use lifecycleScope.launch { flow.collect { } } without cancelling when not visible; that keeps collecting when the screen is gone. repeatOnLifecycle ensures “collect only when STARTED or above”.

---

## launchWhenXxx (deprecated in favor of repeatOnLifecycle)

- **launchWhenCreated/Started/Resumed** — Start when entering that state, suspend (not cancel) when leaving; resume when entering again. For “run when visible” prefer **repeatOnLifecycle(STARTED)** so leaving cancels and semantics are clear.
- **Migration** — Replace launchWhenStarted { flow.collect { } } with repeatOnLifecycle(Lifecycle.State.STARTED) { flow.collect { } }.

---

## LifecycleObserver

- **Purpose** — Run logic at lifecycle events (e.g. register in onStart, unregister in onStop) without overriding many methods in Activity/Fragment.
- **Usage** — Implement **DefaultLifecycleObserver** (or LifecycleEventObserver) and implement onStart/onResume/onStop/onDestroy; register with lifecycle.addObserver(observer), removeObserver when no longer needed.
- **Use cases** — Location updates, sensors, BroadcastReceiver register/unregister, third-party SDK start/stop. Keep the observer stateless or with minimal references to avoid holding Activity and leaking.

---

## Split with ViewModel

- **ViewModel** — Use **viewModelScope** for business data loading, state, and state that survives config change. ViewModel can outlive the View (e.g. rotation), so long-running polling/subscription that is “visible only” can also be in the View layer with repeatOnLifecycle.
- **View layer** — Use **viewLifecycleOwner.lifecycleScope** + **repeatOnLifecycle(STARTED)** for coroutines that must run only while this screen is visible (e.g. collect UI state Flow, polling). One-off work can use lifecycleScope.launch.
