# FoundationTask

An Android sample exploring how to build a **production-ready feature** — a self-contained unit that can be built, tested, and shipped on its own, and removed from the app by deleting its files and a few registration lines without breaking anything else.

The project implements a single product-catalog feature end to end, following a strict layered structure where every file has a clear home and every layer has a clear set of allowed dependencies.

## Tech Stack

Kotlin · Jetpack Compose · Material3 · Hilt · Coroutines & Flow · Retrofit · Compose Navigation (type-safe routes)

## Guiding Principles

Every decision in this project is measured against four goals:

- **Isolation** — the feature compiles, runs, and is tested without knowing about any other feature.
- **Deletability** — removing the feature is mechanical: delete its files, remove a few registration lines, and the app still compiles.
- **Testability** — every layer is testable in isolation. Wide boundary tests (mock the API, assert the UI) are natural, not heroic.
- **Discoverability** — the file tree documents the architecture. Every screen repeats the same shape, so seeing one screen means understanding all of them.

## Feature Structure

The feature is organized into five layers with one-way dependencies:

```
UI → Presentation → Domain → Data
              DI ↕ (wires every layer together)
```

```
feature/
├── data/
│   ├── cache/         → in-memory cache data source (MutableStateFlow)
│   ├── remote/        → remote data source + Retrofit service + DTOs
│   ├── mapper/        → DTO → domain mappers
│   ├── repository/    → repository implementation (cache-aside)
│   └── util/          → handleRequest, typed error handling
├── domain/
│   ├── model/         → domain models (pure Kotlin, no Android)
│   ├── repository/    → repository interfaces
│   └── usecase/       → single-responsibility use cases
├── presentation/      → ViewModels, UiState / UiEvent / SideEffect, UI mappers
├── ui/
│   ├── screens/       → stateless composable screens
│   ├── components/    → reusable composables
│   └── navigation/    → routes, NavHost, nav graph
├── localization/      → localization interface
├── logger/            → logger interface
└── di/                → Hilt modules + feature scope
```

## Main Design Decisions

### Data layer

- The **repository follows the cache-aside pattern**: check the cache, fetch from the network only when needed, store the result, and always emit from the cache flow so the UI observes a single source of truth.
- The **cache stores domain models, not DTOs** — the DTO → domain conversion happens once, not on every read.
- A single **`handleRequest` utility** wraps every network call and translates raw Retrofit/OkHttp/Moshi exceptions into a sealed `DomainException` hierarchy, so upper layers only ever see domain-language errors.
- The repository implementation is `internal` — all access goes through the interface, enforced by the compiler.

### Domain layer

- The domain layer has **no Android dependencies**, so its tests run as plain JVM unit tests.
- **Use cases** expose a single `operator fun invoke(...)` and hold any business logic (filtering, validation) so the ViewModel never changes when that logic evolves.
- Repository **interfaces live here**; the data layer implements them. The domain layer never imports from the data layer.

### Presentation layer

- Every ViewModel exposes exactly three things: a `StateFlow<UiState>`, a `Flow<SideEffect>`, and a single `onEvent(UiEvent)` function (Unidirectional Data Flow).
- Side effects use a `Channel` so one-time events (navigation, messages) are delivered exactly once and never replayed.
- ViewModels receive **use cases, not repositories** — they know nothing about caching or networking.

### Two-stage mapping

```
DTO  ──►  Domain Model  ──►  UI Model
 (API structure)   (business)    (display)
```

Two mappers instead of one. The DTO → domain mapper changes when the API changes; the domain → UI mapper changes when the UI changes. Neither has more than one reason to change.

### UI layer

- Composable screens have a single signature: `(uiState, onEvent)`. They know nothing about ViewModels or `NavController`, which makes them **previewable, testable, and portable**.
- Navigation **routes are type-safe `@Serializable` objects** in a sealed interface — adding a screen means adding a variant the compiler can check.
- The **navigation graph is the boundary** between the infrastructure world (NavController, lifecycle, side-effect collection) and the pure composable world.

### Cross-cutting concerns as abstractions

Localization and logging are **feature-specific interfaces**. The feature declares what it needs; the implementation provides it. Each interface doubles as a complete inventory — every user-visible string, every error path and lets the feature be tested with simple fakes, no Android `Context` required.

## How This Follows the Foundation Principles

| Principle | How it shows up here |
|---|---|
| **Isolation** | The feature depends only on interfaces and its own modules. No imports from other features. |
| **Deletability** | the feature never references another feature's screens directly. |
| **Testability** | Stateless screens, fake localization/logger/toggles, and use cases with no Android dependencies make every layer testable on its own. |
| **Discoverability** | Every screen repeats the same `UiState / UiEvent / SideEffect / ViewModel` shape — learn one, know them all. |
