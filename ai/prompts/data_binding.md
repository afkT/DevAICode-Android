# DataBinding & State Management

This prompt defines how to use XML + DataBinding and how to manage state. It applies to projects using MVVM + ViewModel. Scope, rules, and required operations are stated only in this file.

---

## Scope

- All Activities and Fragments that use XML layouts. View-based UI with DataBinding for UI–data sync.

---

## DataBinding usage

### Layout

- **Root** — Use &lt;layout&gt; wrapper; inner root is the real root View (e.g. ConstraintLayout). In &lt;data&gt; declare variables (e.g. viewModel, item). For non-system types use &lt;import type="package.ClassName" /&gt; or full type.
- **One-way** — @{} for ViewModel/data → View; keep expressions simple; put complex logic in BindingAdapter or ViewModel. Use ?? or default for nullable in expressions to avoid NPE.
- **Two-way** — Use @={} only when View changes must write back (e.g. EditText); avoid circular updates in binding expressions.
- **Variables** — Match project convention (e.g. viewModel → BR.viewModel). If using BaseActivity/BaseFragment, keep bindViewModelId in sync with layout.

### BindingAdapter and BindingConverter

- **BindingAdapter** — For custom attributes, multi-param binding, or logic that needs the View reference (e.g. android:onClick with args). Define in a shared package (e.g. ui/binding).
- **BindingConverter** — For type conversion in layout expressions (Int→String, enum→label). Keep pure, no side effects.
- **Avoid** — Heavy logic, network, or long-lived Context/Activity references in BindingAdapter/Converter to prevent leaks.

### Performance and safety

- **Expression size** — Keep @{} simple; no multi-line logic or unstable external state in expressions.
- **Lifecycle** — Binding is tied to Fragment/Activity lifecycle; avoid references that cause leaks; in list items unbind correctly in ViewHolder.

### Conditional layout

- **ViewStub** — Use ViewStub + DataBinding for blocks that inflate on condition; control from ViewModel (e.g. loading/error/content) instead of complex visibility in XML.

---

## State management

### Choice

- **Expose from ViewModel** — For UI and DataBinding, expose via LiveData in layout and in @BindingAdapter for two-way binding. For non-UI logic, use StateFlow&lt;UiState&gt; or LiveData as needed.
- **LiveData vs StateFlow** — Use LiveData for all UI-facing state: DataBinding, @BindingAdapter, and two-way binding. Use StateFlow for other ViewModel logic and streams. Stay consistent within the project.

### State design

- **Single state object** — Prefer data class UiState (e.g. data class ProfileUiState(val loading: Boolean, val error: String?, val data: Item?)) so loading/error/success are in one place.
- **Immutable updates** — Update via copy() or new object; do not mutate in the UI layer.
- **Boundary** — Separate local UI state (current tab, dialog open) from global/business state (user, list); share across screens via ViewModel or Repository/Flow.

### Coroutines and side effects

- **Coroutines** — Start async in ViewModel viewModelScope; push results to StateFlow/LiveData.
- **One-time events** — For navigation, Toast, Snackbar use SharedFlow/Channel or project event bus so that they are not replayed or triggered multiple times by DataBinding.
