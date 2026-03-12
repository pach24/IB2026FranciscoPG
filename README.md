# IB2026FranciscoPG

An Android app built with Jetpack Compose and MVVM that showcases a invoices experience with mock/live data switching, pull‑to‑refresh, and a clean multi‑module architecture.

**What you’ll see**
- Home screen with greeting, mock mode switch, and latest invoice card
- Invoices flow with Light/Gas tabs, list grouping, and status pills
- Pull‑to‑refresh with Material 3 expressive loading indicator
- Skeleton and shimmer placeholders while loading

**Features**
- Mock mode toggle to instantly switch between mock and “real” sources
- Invoice history grouped and sorted with latest invoice highlight
- Compose‑first UI with reusable components and theme system
- Hilt DI and clean separation between app/domain/data

**Architecture**
- Clean architecture with three modules: `app`, `domain`, `data`
- MVVM in the presentation layer
- Use cases in `domain`, repositories in `data`, UI in `app`

**Tech Stack**
- Kotlin + Jetpack Compose (Material 3)
- Hilt for dependency injection
- Retrofit + Retromock for API and mock responses
- Coroutines for async work
- Navigation Compose

**Project Structure**
- `app`: UI, DI, navigation, theme
- `domain`: business models, repository contracts, use cases
- `data`: API layer, DTOs, repository implementations

**Mock Data**
- Retromock is wired to assets:
  - `app/src/main/assets/invoices_mock.json`
  - `app/src/main/assets/InvoicesMockoonEnvironment.json`
- Toggle mock mode from the Home screen to switch sources

**Base URL**
The base URL is configured in `AppModule`:
- Emulator uses `http://10.0.2.2:3001/`
- Physical device needs your local IP (see comment in code)

**Getting Started**
1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` module on an emulator or device.

**Requirements**
- Android Studio (Giraffe+ recommended)
- JDK 11
- Min SDK 29
