# IB2026FranciscoPG

A native Android application built with **Jetpack Compose**, designed to provide a smooth experience for managing and visualizing energy invoices, featuring real-time data switching and a modular architecture.

> **Project Status:** v1.0.0 – March 2026  

**Clean Architecture • MVVM • Multi-Module • Reactive UI**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-00BFA5?style=for-the-badge&logo=google&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge)

---

## 📄 Overview

This app is a showcase of modern software engineering. The project implements a fully decoupled **three-layer Clean Architecture**, allowing business logic to operate independently from the UI framework or data source.

The project’s key differentiator lies in its **data flexibility**: through a robust dependency injection system, the user can switch between a local (Mock) environment and a real server with a single tap — ideal for technical demos and isolated development environments.

---

## 🖼️ Visual Gallery

Below are the key user interface features:

| **Home & Toggle** | **Invoice List** | **Dark Mode** | **Loading Mode (Skeleton)** |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/dcf1a14e-8e42-484e-8a96-6494a95260fd" width="200" alt="Home Screen"> | <img src="https://github.com/user-attachments/assets/a28f8004-6db8-41c2-8fcc-19d075e1b6b6" width="200" alt="Invoice List"> | <img src="https://github.com/user-attachments/assets/9184baaf-bded-4ed5-a37c-647225b8b08c" width="200" alt="Details"> | <img src="https://github.com/user-attachments/assets/0c46c50d-5581-4269-a2a9-f8d200f2f71e" width="200" alt="Shimmer"> |
| *Dashboard with Mock/Live mode switch.* | *Grouped by type (Electricity/Gas) and status.* | *View in dark mode.* | *Smooth loading experience with Shimmer placeholders.* |

---

## 💡 Key Features

* **🔄 Dual Data Source Strategy:** Instantly switch between `Retrofit` (real API) and `Retromock` (local JSON) without restarting the app.  
* **📊 Smart Invoice Management:** Optimized listing with category grouping (Electricity/Gas), chronological sorting, and visual highlight for the latest invoice received.  
* **✨ Modern UI/UX:**
  * Fully built with **Material 3**.
  * **Pull-to-refresh** with expressive loading indicators.
  * **Skeleton Loading** and Shimmer for instant loading perception.
* **🏗️ Strict Modularization:** Physical separation of responsibilities to improve build times and ensure scalable code.

---

## 🏛️ Architecture

The project follows **SOLID** principles and Google’s official recommendations for building robust applications.

### 🧩 Module Structure

1. **`:app` (Presentation Layer):** Contains the UI (Compose), ViewModels, and Hilt configuration. Implements **UDF (Unidirectional Data Flow)** via `StateFlow`.  
2. **`:domain` (Business Layer):** Pure Kotlin module. Defines business entities, repository contracts (interfaces), and **Use Cases**.  
3. **`:data` (Data Layer):** Repository implementations, DTOs, Retrofit services, and the logic for data source switching.

---

## 🚀 Tech Stack

* **Language:** Kotlin + Coroutines & Flow  
* **UI Framework:** Jetpack Compose (Material 3)  
* **Dependency Injection:** Hilt (Dagger) for clean and testable dependency management  
* **Networking:** Retrofit + OkHttp + Retromock  
* **Navigation:** Navigation Compose with type-safe route handling  
* **State Management:** MVVM with `StateFlow` and `collectAsStateWithLifecycle()`

---

## 💾 Installation and Usage

### Prerequisites

* Android Studio Giraffe or higher  
* JDK 11  
* Min SDK: 29  

### Steps

1. **Clone the repository:**
    ```bash
    git clone https://github.com/tu-usuario/IB2026FranciscoPG.git
    ```
2. **API Configuration:**
    For “Live” mode, configure your local IP in `AppModule` if using a physical device. By default, it points to `10.0.2.2:3001` (the emulator’s localhost).  
3. **Run:** Sync Gradle and launch the `:app` module.

---

## 📚 Lessons Learned

* **Dynamic Injection:** Implementing Hilt to swap data providers at runtime.  
* **Complex State Management:** Using `Sealed Interfaces` to represent UI states (`Loading`, `Success`, `Error`), eliminating impossible states.  
* **Modularization:** Benefits of isolating business logic (`domain`) in a pure Kotlin module with no Android dependencies, simplifying unit testing.

---
