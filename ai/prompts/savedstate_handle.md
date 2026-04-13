# SavedStateHandle (State Restoration and Process Death)

This prompt defines how to save and restore state in the ViewModel via SavedStateHandle and how data behaves after process death. It applies to projects using MVVM + ViewModel. Scope, rules, and required operations are stated only in this file.

---

## Scope

- UI state, form input, scroll position, selected tab, etc. that must survive configuration change (e.g. rotation) or process death. It does not cover long-term persistence to disk (use Room or DataStore for that).

---

## SavedStateHandle basics

- **Source** — The ViewModel constructor can take **SavedStateHandle**. AndroidX ViewModel injects it when creating the ViewModel; Activity/Fragment `savedInstanceState` and `arguments` are merged into the handle.
- **Read/write** — `get<String>("key")`, `getLiveData<String>("key")`, `set("key", value)`. Supports primitives and **Parcelable**, **Serializable**, **ArrayList&lt;String&gt;**; for complex objects prefer storing only an id and reloading from Repository on restore.
- **LiveData** — `getLiveData("key", default)` returns LiveData tied to the handle; writing to the handle updates LiveData so the UI can observe directly.

---

## When to use

- **State to restore** — Current screen’s temporary UI (e.g. EditText content, list scroll, expand/collapse), or user choices in the current flow not yet committed. Do not use for “already persisted” business data (reload from Repository/DB).
- **Process death** — The system may kill the process under memory pressure. When the Activity is recreated, the ViewModel is recreated; if it was constructed with SavedStateHandle, data previously written with `set()` is restored from the system Bundle.
- **Configuration change** — Rotation, multi-window, etc. recreate Activity/Fragment but the ViewModel is usually kept. Without SavedStateHandle, state survives only across configuration change. For consistency and process-death recovery, save important state through the handle.

---

## When and what to save

- **Write** — Call `savedStateHandle.set("key", value)` when state changes (e.g. after user input, selection, scroll). Do not write on every `uiState` update; save only the minimal set needed for restore.
- **Keys** — Use constants or a shared prefix to avoid clashing with argument keys (e.g. `STATE_SCROLL_POSITION`, `STATE_FORM_INPUT`).
- **Size** — SavedStateHandle is serialized into a Bundle. Avoid large objects or long lists; store id, position, simple form fields. For lists use “page + scroll position” or id list and reload details on restore.

---

## With Arguments / Intent

- **Launch args** — Id, type, etc. from Activity Intent or Fragment arguments are in SavedStateHandle. The ViewModel can use `savedStateHandle.get<Long>("id")` after process death to get launch parameters.
- **Navigation Safe Args** — With Safe Args, arguments are merged into the handle. The ViewModel can read from the handle or Fragment can use `by navArgs()`; args are still there after recreate.
- **Boundary** — Put only “what’s needed to restore the screen” in the handle. Load business data (list items, details) from Repository by id so there is a single source of truth.

---

## Testing

- **Unit** — Construct the ViewModel with `SavedStateHandle().apply { set("key", value) }` and assert restored state or load logic; assert that after `set()`, `getLiveData().value` or next `get()` returns the right value.
- **Process death** — Use “Don’t keep activities” or adb to kill the process and return; verify that important screen state is restored.
