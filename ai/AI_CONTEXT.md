# AI Context: Android App (XML + DataBinding)

Android app: Kotlin, XML layouts, DataBinding, MVVM, Room, Hilt, Retrofit.

## Goals

- Kotlin-first (Java when required)
- MVVM + DataBinding + ViewModel + Lifecycle
- Offline-first; local DB as source of truth
- Clear domain / data separation
- Modularization: feature, shared, app modules
- Testability across layers
- Component-based: reuse, maintainability, separation of concerns

## Tech Stack

- **UI:** XML layouts, View-based, DataBinding (no Compose)
- **Architecture:** ViewModel, Lifecycle, Navigation Component, Safe Args
- **DI:** Hilt
- **Async:** Kotlin Coroutines, Flow, StateFlow, LiveData
- **Persistence:** Room + Flow
- **Networking:** Retrofit
- **State:** DataBinding + LiveData (UI and two-way binding); StateFlow for other logic
- **Base:** BaseActivity, BaseFragment, BaseViewModel

## Modules / Layers

- **data/** — Room, DAOs, DTOs, network, repository impl, mappers
- **domain/** — Models, repository interfaces, use cases
- **ui/** — Activities, Fragments, ViewModels, XML layouts, binding adapters
- **di/** — Hilt modules

## Modularization

- Feature, shared, app modules; interfaces to decouple
- Base components and utilities in shared; custom views/ViewHolders for cross-cutting
- Inter-module via DI

## Data & Boundaries

- Domain models in `domain/model/`; do not leak Room/DTO into UI or domain
- Repository interfaces in `domain/repository/`; implementation in `data/repository/`
- Mapping at boundaries (e.g. `data/mapper/`); domain and data testable without Android
- Screen args: pass id or minimal keys; load full data from Repository (single source of truth)

## Screen & Performance

- **Screen:** ConstraintLayout, sp/dp, qualifiers (sw, w, orientation), WindowInsets; test multiple configurations
- **Perf:** RecyclerView + DiffUtil, ViewStub, Room indexing + background, ProGuard/R8

## Kotlin & Coroutines

- Coroutines, Flow, sealed classes, suspend; exception and cancellation handling; functional style when it helps readability

## Security (baseline)

- Sensitive data: EncryptedSharedPreferences or Keystore; no plain tokens/passwords in storage
- HTTPS only (no plain HTTP in production); no sensitive data in logs or crash payloads

## Project Workflow

- Feature branches; clear commits; atomic changes
- Code review: performance, security, maintainability

## What AI Should Do

- Propose diffs, not full-file rewrites
- Follow project patterns and stack above
- Ask when context is missing
- Add tests with new features when feasible
