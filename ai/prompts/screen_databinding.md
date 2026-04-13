# XML + DataBinding Screen (New Screen)

This prompt defines the steps and checks for creating a full screen (Activity or Fragment) from scratch with XML layout and DataBinding. It applies to projects using MVVM, DataBinding, and ViewModel. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Use when adding a new screen with XML layout, DataBinding, and ViewModel. If the project has BaseActivity/BaseFragment and routing conventions, follow those as well.

---

## Deliverables

- **UI** — Activity or Fragment (extend project Base*), XML layout (`<layout>` + `<data>` + root View), optional BindingAdapter/Converter.
- **State and logic** — ViewModel (Hilt-injected), UiState (e.g. data class), Repository calls and state updates.
- **Navigation and events** — Navigation events (back, next screen) consumed via callback or SharedFlow; no business logic in the View.
- **Tests** — ViewModel unit tests (success, failure, loading).

---

## Steps and rules

### 1. ViewModel and UiState

- **ViewModel** — Use Hilt `@HiltViewModel` and constructor-inject Repository; launch coroutines in `viewModelScope`; expose `StateFlow<UiState>` or LiveData.
- **UiState** — Use immutable state, e.g. `data class XxxUiState(val loading: Boolean, val error: String?, val data: T?)`; update via `stateFlow.update { }` or MutableStateFlow.copy().
- **Logic** — All “where to get data, how to compute, what to do on failure” lives in ViewModel or UseCase; Activity/Fragment only observes state, calls ViewModel methods, and handles navigation/one-time events.

### 2. XML layout

- **Root** — `<layout>` wrapper, then actual root (e.g. ConstraintLayout); in `<data>` declare `variable name="viewModel" type="package.XxxViewModel"`.
- **Binding** — One-way `@{}`, two-way `@={}`; keep expressions simple; put complex logic in BindingAdapter or ViewModel.
- **State** — Drive loading/error/empty via viewModel.uiState (visibility or ViewStub). For lists use RecyclerView and project Adapter conventions if any.
- **Base** — If the project has BaseActivity/BaseFragment (e.g. bindLayoutId, BR.viewModel), extend them and pass the correct layout and ViewModel BR.id.

### 3. Activity / Fragment

- **DataBinding** — In onCreate/onCreateView get Binding and set `binding.viewModel = viewModel` (if the base class does not). Observe viewModel.uiState to update UI or rely on layout binding.
- **Lifecycle** — In onDestroy/onDestroyView cancel subscriptions and clear listeners; do not hold Repository or UseCase in the View layer.
- **Navigation** — Observe ViewModel navigation (e.g. SharedFlow/LiveData) and call Navigation or startActivity; do not put business logic here.

### 4. Dependency injection

- **Hilt** — ViewModel from Hilt; Repository provided in data layer module with @Binds/@Provides; ViewModel constructor injection.
- **Koin** — In the module: `viewModel { XxxViewModel(get()) }`; in the screen: `by viewModel()`.

### 5. State and errors

- **Loading** — Set loading = true when starting request; loading = false when done (success or failure).
- **Error** — Set error = "user-facing message" on failure; provide retry (e.g. button) that calls ViewModel load again.
- **Empty** — Show empty state view when list is empty; keep it distinct from error.

### 6. Optional

- **BindingAdapter** — Use for non-standard attributes (e.g. custom view `app:customAttr`); use BindingConverter for complex conversion.
- **ViewStub** — For conditional blocks use ViewStub + binding; control from UiState which block to show.
- **Accessibility** — Set android:contentDescription on icons and key actions; touch targets ≥ 48dp; consider announceForAccessibility for state changes.
- **Screen adaptation** — Use res/dimens and ConstraintLayout Guideline/Barrier; use values-sw*dp etc. as needed; handle WindowInsets for notch and nav bar.

---

## Quality checklist (self-check or code review)

- [ ] No business logic in Activity/Fragment (only observe and forward).
- [ ] ViewModel uses Repository, not DAO/API directly.
- [ ] Layout uses `<layout>` and variable; BR.id matches Base convention if used.
- [ ] Loading, error, and empty have corresponding UI and state.
- [ ] Navigation and one-time events are exposed from ViewModel and consumed in UI.
- [ ] ViewModel has unit tests (success, failure, loading).
- [ ] Accessibility and screen adaptation checked where needed.
- [ ] Nullable types and defaults in layout are correct; no NPE risk.
