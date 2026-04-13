# Tests (Unit / Integration / UI / Performance)

This prompt defines test scope, frameworks, and style for projects using MVVM, Repository, and ViewModel. Goal: prioritize unit tests in layers that do not depend on the Android framework; add integration and UI tests when needed. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Test requirements when adding or changing ViewModel, Repository, UseCase, utilities, or network/DB boundaries. Prioritize unit tests for ViewModel, Repository, UseCase; prefer Fakes over heavy mocks.

---

## Unit tests

### Framework and dependencies

- **JUnit** — JUnit 4 or 5 (match project); use assertThat or JUnit 5 assert*.
- **Mockito / MockK** — Per project; Kotlin projects often prefer **MockK** for coroutines and final classes.
- **Coroutines** — If code uses suspend/Flow, use **CoroutineTestRule** or **runTest** (kotlinx-coroutines-test) so tests control Dispatcher and timing.

### Structure and naming

- **Structure** — Given/When/Then or AAA (Arrange, Act, Assert); one behavior per test.
- **Naming** — Describe “under what condition, what happens, what is expected”, e.g. whenUserNotFound_shouldEmitErrorState or loadProfile_success_returnsData.

### Subject and doubles

- **ViewModel** — Inject Fake Repository or MockK; collect StateFlow/LiveData and assert state and side effects (e.g. navigation).
- **Repository** — Inject Fake DAO / Fake API or MockK; assert call count/args and Flow/suspend result.
- **UseCase** — Inject Fake Repository; assert input→output and error paths.
- **Utilities** — Pure functions: test directly; if they use Context/system API use mocks or Android test deps (e.g. robolectric) as needed.
- **Doubles** — Prefer **Fake** (lightweight real behavior) over complex **Mock** for readability and stability.

### Scenarios

- **Success** — Normal input and dependency return.
- **Failure** — Network error, empty data, exception, permission; assert error state or exception type.
- **Boundaries** — Empty list, empty string, edge values.

### Example request (for AI)

```
Add unit tests for XxxViewModel loadData / submit:
- Success: UiState has data, loading false
- Failure: UiState has error, loading false
- Use FakeXxxRepository or MockK; runTest for coroutines
```

### Test data

- **Fake** — Lightweight Fake for Repository, DAO, API; no real external systems.
- **Factories** — Use factories for test data; avoid large inline objects.
- **Cleanup** — Clean up after each test; use @After or rules for isolation.

### Coverage and quality

- **Target** — Aim for 80%+ line coverage on core logic; ViewModel and UseCase first.
- **Boundaries** — Empty list, null, edge values, invalid input.
- **Performance** — Avoid O(n²) in tests with large data where it would hide issues.

---

## Integration tests

- **Goal** — Verify collaboration and data flow (e.g. Repository + DAO + in-memory DB, or Repository + Retrofit + MockWebServer).
- **Database** — Use Room **in-memory** DB; schema changes need **migration** and test execution.
- **Network** — **MockWebServer** (OkHttp) to simulate API; assert path/body and response→domain mapping.
- **Scope** — Focus on key flows (login, order, sync); avoid huge integration suites that slow the build.
- **Data** — Use real structures with controlled size; do not depend on live external data.

---

## UI tests (instrumented / Espresso)

- **Scope** — Critical paths (launch to first screen, list to detail, form submit).
- **Tools** — Espresso for key paths; UiAutomator for cross-process or system UI when needed.
- **Stability** — Use IdlingResource or coroutine test utilities to wait for async work; avoid fixed Thread.sleep.
- **Maintenance** — UI tests are brittle; prioritize critical path; rely on unit tests + a few smoke UI tests for complex UI.

---

## Performance and benchmarks

- **Startup and runtime** — **Macrobenchmark**, **UiAutomator** for startup and frame rate; use in CI for regression baselines.
- **DB / network** — Load tests for large list, paging, concurrency as needed; can combine with integration tests.
