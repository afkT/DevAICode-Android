# Modularization (Modules and Component Architecture)

This prompt defines how to split modules, dependency direction, and cross-module communication. It applies to projects with data/domain/ui/di layers and Hilt. Scope, rules, and required operations are stated only in this file.

---

## Scope

- Adding feature or shared modules or changing app vs library boundaries. Inside a single module still use data/domain/ui/di package structure.

---

## Module types and roles

- **app** — Shell; Application, navigation entry, wiring of features; depends on feature and shared; does not depend on feature implementation details.
- **feature** — By capability (e.g. login, profile, order); can contain that feature’s data/domain/ui; expose only what is needed via interface or Hilt; do not expose implementation.
- **shared / core** — Common code (network, DB, base UI, utils, base classes); no dependency on specific features; used by app and features.
- **domain** (optional) — If a separate module: models, Repository interfaces, UseCases only; no Android dependency so JVM tests are easy.

---

## Dependency direction and decoupling

- **Direction** — app → feature → shared; shared and domain do not depend on app/feature. No direct feature→feature dependency; use interface or app wiring.
- **Abstraction** — Cross-module capabilities (e.g. “open screen X”, “get user info”) defined as interfaces in shared or domain, implemented in feature or app; inject via **Hilt** or “interface + implementation registration”; avoid app importing feature.XxxActivity.
- **Data** — Cross-module args via Safe Args, Intent extra, or shared domain model; do not pass Room entity or DTO across modules; map at boundaries.

---

## Components and API design

- **Shared UI** — Custom View, ViewHolder, Dialog, base ViewModel in shared; clear API and KDoc; do not expose internals.
- **Cross-module navigation** — Centralize in Navigation graph or route table; or use a router (e.g. ARouter/TheRouter) with path→target registered in app; feature only declares path, does not depend on target Activity/Fragment class.
- **Resources** — Shared string/dimen/theme in shared; feature uses own or shared resources only; no feature→feature resource dependency.

---

## Testing and build

- **Unit** — domain and data layers have no Android dependency; run on JVM. Feature modules can run their unit tests independently.
- **Build** — Use Gradle module dependencies and productFlavor to control what is compiled; avoid circular dependencies; use **api** vs **implementation** to control transitive deps.
- **Packages** — Package names match modules (e.g. com.xxx.feature.login.ui); avoid same package name across modules.
- **Changes** — Confirm with team before renaming packages or modules to avoid breaking dependents and history.
