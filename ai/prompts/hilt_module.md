# Hilt Module (Dependency Injection)

This prompt defines how to declare Hilt modules, scopes, and how to provide Database/DAO/Repository/API and ViewModel. It applies to projects with data/domain/ui layers. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding or changing Hilt modules and providing Repository/ViewModel/network/database. Use when the project uses Hilt for DI; prefer constructor injection.
- **Prerequisite** — Application class must be annotated with `@HiltAndroidApp`; Activity/Fragment that use Hilt must be annotated with `@AndroidEntryPoint`, or injection will not work.

---

## Module definition

- **Annotations** — Use `@Module` + `@InstallIn(XXXComponent::class)` to declare the component; split by layer or feature (e.g. NetworkModule, DatabaseModule, XxxFeatureModule).
- **Interface binding** — For Repository, API interfaces → implementation prefer **@Binds** (only returns implementation, no extra logic) over @Provides when possible.
- **Instance provision** — When @Binds is not possible (e.g. built from Builder, third-party) use **@Provides**; method parameters are injected by Hilt.
- **Scope** — For singleton or scoped lifetime, annotate the provider method or implementation with the matching **@XxxScoped** (see below).

---

## Scopes and components

| Scope           | Component          | Typical use |
|-----------------|--------------------|-------------|
| @Singleton      | SingletonComponent | App-wide: Database, OkHttp, Retrofit, global Repository |
| @ActivityScoped | ActivityComponent  | Per-Activity singleton (when needed) |
| @FragmentScoped | FragmentComponent  | Per-Fragment singleton (when needed) |
| @ViewModelScoped| ViewModelComponent | Per-ViewModel (single instance per ViewModel) |
| @ServiceScoped  | ServiceComponent   | Per-Service singleton |

- **Principle** — Use Scope only for “expensive or shared state”; lightweight stateless types (Mapper, Formatter) can be unscoped @Provides (new instance per injection unless held by a Singleton).
- **ViewModel** — Use **@HiltViewModel** and constructor injection; do not write ViewModel @Provides in a Module. Activity/Fragment get ViewModel via `by viewModels()` or Hilt’s ViewModelProvider.

---

## Context injection

- **Application** — Use `@ApplicationContext`-annotated Context (provided by Hilt or qualifier).
- **Activity** — Use `@ActivityContext` in Activity or Activity-scoped Module.
- **Caution** — Do not hold Activity Context in a Singleton (leak risk); use Application Context for long-lived objects.

---

## Qualifiers

- **Multiple implementations** — When the same type has several implementations (e.g. multiple Retrofit, Repository) use **@Named("name")** or a custom **@Qualifier**; use the same on the @Provides method and at the injection site.
- **Custom** — e.g. @RetrofitAuth, @RetrofitPublic for type-safe distinction.

---

## Common provisions

- **Room Database** — In a Singleton module: `@Provides @Singleton fun provideDb(@ApplicationContext ctx: Context): AppDatabase`; provide DAO via db.xxxDao() or separate @Provides.
- **Retrofit / OkHttp** — Provide OkHttpClient, Retrofit, API in NetworkModule; API can be @Binds to implementation or @Provides returning retrofit.create(XxxApi::class.java).
- **Repository** — Interface in domain, implementation in data; in data or feature module: `@Binds abstract fun bindXxxRepository(impl: XxxRepositoryImpl): XxxRepository`.
- **ViewModel** — Only annotate the ViewModel class with @HiltViewModel; no ViewModel @Provides in Module.

---

## Best practices

- **Constructor injection** — Prefer @Inject on constructor; avoid field injection unless constructor is not possible.
- **No cycles** — If A→B and B→A, introduce an interface or **Lazy&lt;T&gt;** on one side.
- **Non-Hilt** — For WorkManager, BroadcastReceiver, etc. use **@EntryPoint** to obtain dependencies, or EntryPointAccessors.fromApplication() etc.
- **Organization** — Organize modules by layer (network, database, repository) or feature; avoid one huge module for everything.

---

## Example request (for AI)

```
Add Hilt binding for XxxRepository: interface in domain.repository, implementation in data.repository.
- In data module: @Binds XxxRepository → XxxRepositoryImpl.
- Ensure XxxRepositoryImpl constructor parameters (e.g. DAO, Api) are provided by other modules.
- ViewModel uses @HiltViewModel and constructor-injects XxxRepository.
```
