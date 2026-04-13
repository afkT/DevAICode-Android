# Koin Module (Dependency Injection)

> **Note:** This project defaults to **Hilt** for dependency injection (see AGENTS.md). Use this prompt **only** when working on modules or sub-projects that explicitly use Koin. For Hilt-based modules, follow `hilt_module.md` instead.

This prompt defines how to declare Koin modules, singletons/factories/ViewModels, and how to provide Database/DAO/Repository/API. It applies to projects with data/domain/ui layering. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding or changing Koin `module { }` and providing Repository/ViewModel/network/database dependencies. Use when the project or sub-module explicitly uses Koin for DI.

---

## Module definition

- **Declaration** — Use `module { }` for a set of definitions. Split by layer or feature (e.g. `networkModule`, `databaseModule`, `xxxFeatureModule`) and pass them in Application or tests: `startKoin { modules(listOf(...)) }`.
- **Constructor injection** — Koin resolves constructor parameters by reflection or KSP; no manual @Provides. Declare how to obtain the type (single/factory/viewModel) and optional parameters.

---

## Definition styles

### single

- **Use** — One instance for the Application lifetime: Database, OkHttpClient, Retrofit, global Repository.
- **Syntax** — `single { XxxRepositoryImpl(get(), get()) }`; `get()` resolves declared dependencies. For lazy creation use `single(createdAtStart = false) { ... }`.
- **Interface** — `single<XxxRepository> { XxxRepositoryImpl(get()) }` or `single { get<XxxRepositoryImpl>() as XxxRepository }`; prefer the first.

### factory

- **Use** — New instance per request: stateless, lightweight types (Mapper, Formatter, Adapter, one-off DTO converters).
- **Syntax** — `factory { XxxMapper() }`; if the constructor needs dependencies, `factory { XxxFormatter(get()) }`.

### viewModel

- **Use** — ViewModel, scoped to Activity/Fragment lifecycle; one per scope.
- **Syntax** — `viewModel { XxxViewModel(get()) }`. In Activity/Fragment use `by viewModel()` or `getViewModel()`.
- **Params** — For runtime params (e.g. id from Intent) use `viewModel { (id: String) -> XxxViewModel(get(), id) }` with `parametersOf(id)` or Koin’s `stateViewModel`/`sharedViewModel` as needed.

---

## Common declarations

- **Room Database** — `single { Room.databaseBuilder(get(), AppDatabase::class.java, "db").build() }`. Provide Application or Context (e.g. `single { this.androidContext() }` or `androidContext(application)` in startKoin).
- **DAO** — `single { get<AppDatabase>().xxxDao() }`.
- **Retrofit / OkHttp** — `single { createOkHttpClient() }`, `single { createRetrofit(get()) }`, `single { get<Retrofit>().create(XxxApi::class.java) }`.
- **Repository** — `single<XxxRepository> { XxxRepositoryImpl(get(), get()) }`; interface in domain, implementation in data.
- **ViewModel** — `viewModel { XxxViewModel(get()) }`; in the screen: `private val vm: XxxViewModel by viewModel()`.

---

## Qualifiers and naming

- **Same type** — Use **named**: `single(named("auth")) { createAuthRetrofit(get()) }`; inject with `get(named("auth"))` or `parametersOf(named("auth"))`.
- **Qualifier** — You can use an object like `object AuthApi` as qualifier with `single<Retrofit>(AuthApi) { ... }`.

---

## Scopes (optional)

- **Default** — single is root-scope singleton; viewModel is tied to Android lifecycle.
- **Custom** — For Activity- or Fragment-scoped singletons use `scope<Activity> { ... }`; often single + factory + viewModel is enough.

---

## Android integration

- **Application** — In `Application.onCreate`: `startKoin { androidContext(this); modules(...) }`.
- **Context** — In modules use `get<Context>()` or `androidContext()` (must be set in startKoin).
- **ViewModel** — Use `org.koin.androidx.viewmodel`’s `viewModel()`, `by viewModel()`; compatible with AndroidX ViewModel.

---

## Best practices

- Use **single** only for expensive or shared-state dependencies (Database, network client, Repository). Use **factory** for lightweight objects.
- Use **viewModel** for all screen ViewModels so lifecycle and state restoration work.
- Organize modules by layer or feature (network, database, repository, viewmodel) so tests can override (e.g. in tests: `module { … override single<XxxRepository> { FakeXxxRepository() } }`).
- Avoid circular dependencies; use interfaces or `get()` lazy resolution if needed (Koin can resolve some cycles lazily).

---

## Example request (for AI)

```
Add Koin declaration for XxxRepository: interface in domain, implementation in data.
- In repositoryModule or dataModule: single<XxxRepository> { XxxRepositoryImpl(get(), get()) }.
- Ensure XxxRepositoryImpl constructor parameters (DAO, Api) are declared in other modules.
- ViewModel: viewModel { XxxViewModel(get()) }; in screen use by viewModel().
```
