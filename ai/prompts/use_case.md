# UseCase (Use Case Layer)

This prompt defines the responsibility, style, and boundaries of UseCases with Repository and ViewModel. It applies to projects using MVVM and layered architecture. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Introduce a UseCase layer when business logic is reused in multiple places or when “one user action → one flow” should be extracted from the ViewModel for single-responsibility and testability. UseCases are optional; add them only when they clearly improve reuse, testability, or readability.

---

## Responsibility and placement

- **Single responsibility** — One UseCase = one business operation or user intent (e.g. “get user detail”, “submit order”, “sync local and remote”). It may call one or more Repositories and orchestrate order, parameter mapping, and error mapping.
- **No UI, no Android** — UseCase does not depend on Activity/Fragment, Context, or View; only on Repository interfaces, other UseCases, or pure Kotlin utilities. This allows JVM unit tests without Android.
- **Stateless** — UseCase is typically stateless; each call depends only on inputs and Repository results; no mutable “session” state. Cache in the Repository layer if needed.
- **In domain layer** — Put UseCase classes in the domain package (e.g. `domain.usecase`). They depend on Repository interfaces (domain); implementations live in data. ViewModel depends on UseCase instead of multiple Repositories to keep ViewModel focused. If there is no separate domain module, keep UseCases in the feature’s domain package and still avoid Android/UI so JVM tests work.

---

## Style and conventions

- **invoke** — Implement `operator fun invoke(...): Flow<T>` or `suspend fun invoke(...): Result<T>`; call as `useCase(params)` or `useCase.invoke(params)`.
- **Input/output** — Input: primitives, data class, or value object. Output: `Flow`, `suspend`, or `Result`/sealed class. Do not expose Room entity or DTO from UseCase; map inside or delegate to Repository returning domain models.
- **Errors** — In UseCase, catch exceptions and map to `Result.failure` or sealed Error; do not throw raw exceptions to ViewModel. If Repository already returns Result, UseCase only composes and maps.
- **Coroutines** — UseCase methods are `suspend` or return `Flow`; do not take CoroutineScope; the caller (ViewModel) launches or collects in `viewModelScope`. UseCase does not own lifecycle.

---

## Boundary with Repository / ViewModel

- **Repository** — Offers atomic operations (single query, single write). UseCase composes multiple Repository calls, branching, validation, and error mapping (e.g. “submit order” UseCase: check stock via Repository, then submit via Repository, with unified error type).
- **ViewModel** — Calls UseCase for data or actions and maps results to UiState. Do not put multi-step Repository orchestration or complex branching in ViewModel; keep ViewModel to “call UseCase, observe result, map to UI state”.
- **When not to add UseCase** — For a single Repository call with no reuse and no complex logic, call Repository directly from ViewModel; avoid extra UseCase layers for trivial cases.

---

## DI and testing

- **Constructor injection** — UseCase constructor takes Repository interface (and optionally other UseCases). Provide the UseCase via Hilt/Koin in domain or data layer; inject UseCase in ViewModel.
- **Unit tests** — Inject a Fake Repository (fixed data or configurable Flow/Result) and assert invoke inputs/outputs and Repository call count/arguments; no Android, fast runs.
- **Naming** — Class name describes the action, e.g. `GetUserProfileUseCase`, `SubmitOrderUseCase`; method can be `invoke` or a specific name (e.g. `execute`) per project convention.
