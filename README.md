# IberQube

Aplicación nativa Android construida con **Jetpack Compose** para la gestión y visualización de facturas energéticas, con cambio de fuente de datos en tiempo real y arquitectura modular.

> Mayo de 2026 (Entrega 4)

**Clean Architecture · MVVM · Multi-módulo (`:app`, `:presentation`, `:domain`, `:data`) · UI reactiva · Firebase**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge\&logo=jetpackcompose\&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge\&logo=android\&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge)
![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge\&logo=firebase\&logoColor=black)

---

## Descripción general

Aplicación que implementa una arquitectura **Clean Architecture multi-módulo** (`:app`, `:presentation`, `:domain`, `:data`), permitiendo que la lógica de negocio opere de forma independiente al framework de UI o a la fuente de datos.

El diferenciador clave es la **flexibilidad de datos**: mediante inyección de dependencias, el usuario puede alternar entre un entorno local (RetroMock) y un servidor real (Retrofit + Mockoon) con un solo toque, ideal para demos técnicas y entornos de desarrollo aislados.

---

# 🆕 What’s New – Entrega 4

Entrega centrada en la integración de Firebase.

### 🔥 Integración Firebase

* **Firebase Analytics:** Infraestructura de tracking centralizada (`AnalyticsTracker` en `:domain`, `FirebaseAnalyticsTracker` en `:data`) con eventos que cubren toda la app:

  * Vistas de pantalla (`screen_view`) en Home, Mis Facturas, Factura Electrónica y el wizard de activación/modificación.
  * Interacciones con facturas: apertura de detalle, apertura de feedback y envío de valoración.
  * Acciones sobre filtros: apertura, aplicación, limpieza, cambio de tipo de suministro y selección de estado.
  * Pasos del wizard de Factura Electrónica: avance de paso, reenvío de SMS, finalización y cancelación.
* **Firebase Remote Config:** Feature flag `gas_contracts_enabled` que controla en tiempo real la visibilidad de los contratos de Gas en la pantalla de Factura Electrónica. Intervalo de fetch configurado a 0 s en debug para una iteración rápida.
* **Firebase Crashlytics (infraestructura):** Clase `FirebaseCrashReporter` preparada (implementa `CrashReporter` del dominio), pendiente de activación completa en una entrega posterior.

---

## Galería visual

### Flujo de factura electrónica

> Flujo de Factura Electrónica y componentes UX comunes.

|                                                   Pantalla de contratos                                                  |                                                    Wizard — Email + legal                                                    |                                                 Wizard — Código OTP                                                |
| :----------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/5b2e9394-bbe3-4047-8796-4b7b11cd16c1" width="200" alt="Contratos"/> | <img src="https://github.com/user-attachments/assets/eac865d5-31c4-430e-a335-db550770d035" width="200" alt="Email + legal"/> | <img src="https://github.com/user-attachments/assets/3f230f25-e626-4104-8e67-defc8c5e79f8" width="200" alt="OTP"/> |

|                                             Pantalla de éxito (modificar)                                            |                                                         Modificar email                                                        |                                              Pantalla de éxito (activar)                                             |
| :------------------------------------------------------------------------------------------------------------------: | :----------------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/e7fe35d5-6a40-4eb0-a1b0-2d87f76f803e" width="200" alt="Éxito"/> | <img src="https://github.com/user-attachments/assets/5b67a719-9356-4120-afd8-b5fa30869265" width="200" alt="Modificar email"/> | <img src="https://github.com/user-attachments/assets/ee33c344-49d9-4f3c-a377-9afc72cb6825" width="200" alt="Extra"/> |

---

<details>
<summary><strong>📱 Pantallas principales</strong></summary>

<br>

|                                               Home y toggle                                              |                                             Lista de facturas                                            |                                             Carga (Skeleton)                                             |                                               Bottom Sheet                                               |
| :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/7d5016e4-bb5d-49a1-bf66-2efb6a4ef19d" width="200"/> | <img src="https://github.com/user-attachments/assets/700552ba-a6e1-4fb5-acd8-2bb5809a8daf" width="200"/> | <img src="https://github.com/user-attachments/assets/72041eaa-bfa3-497b-ac73-c1ddcd86d82a" width="200"/> | <img src="https://github.com/user-attachments/assets/f05b74c3-7565-4396-af56-73417e326141" width="200"/> |

</details>

---

<details>
<summary><strong>🎨 Filtros y estados</strong></summary>

<br>

> Nuevas funcionalidades centradas en filtrado, estados y experiencia de usuario.

|                                            Pantalla de filtros                                           |                                                Modo oscuro                                               |                                              Lista filtrada                                              |                                                Empty State                                               |
| :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------: |
| <img src="https://github.com/user-attachments/assets/a770b86b-d78f-4845-a63c-93a66256d930" width="200"/> | <img src="https://github.com/user-attachments/assets/c7f86169-52ab-4709-bedb-be88341b14e6" width="200"/> | <img src="https://github.com/user-attachments/assets/bfd64d74-c1ea-4128-ae86-ed3500dc9df0" width="200"/> | <img src="https://github.com/user-attachments/assets/9fbe45e5-9ff1-4934-8ed7-3ec06227219d" width="200"/> |

</details>

---

## Funcionalidades principales

* **Doble fuente de datos:** Cambio instantáneo entre `Retrofit` (API real vía Mockoon) y `RetroMock` (JSON local) sin reiniciar la app.
* **Gestión inteligente de facturas:** Listado optimizado con agrupación por categoría (Luz/Gas), ordenación cronológica y destacado visual de la última factura.
* **UI/UX moderna:**

  * Construida íntegramente con **Material 3 Expressive**.
  * **Pull-to-refresh** con indicadores de carga expresivos en todos los estados (datos, vacío y error).
  * **Skeleton Loading** con Shimmer para una percepción de carga instantánea.
  * Auto-switch de pestañas inteligente (si un tipo de suministro está vacío, navega automáticamente al que tiene datos).
* **Estados de error diferenciados:** Error de servidor y error de conexión con iconografía y mensajes distintos, botón de reintento y pull-to-refresh.
* **Factura Electrónica:** Gestión de contratos por suministro y wizards de **activación** y **modificación de email** (4 pasos cada uno) con validación en tiempo real, verificación SMS, loading overlay y banners auto-dismissibles.
* **Splash Screen:** Pantalla de carga inicial con tema dedicado.
* **Firebase Analytics:** Tracking de navegación e interacciones de usuario a través de una abstracción en `:domain` (`AnalyticsTracker`) implementada en `:data` con el SDK de Firebase, sin acoplar la UI al proveedor.
* **Firebase Remote Config:** Flag `gas_contracts_enabled` que activa o desactiva la sección de contratos de Gas sin necesidad de una nueva publicación en Play Store.
* **Firebase Crashlytics:** Infraestructura preparada para el reporting de crashes en producción mediante `FirebaseCrashReporter` (implementa `CrashReporter` del dominio), con activación completa prevista en una entrega posterior.
 
## Requisitos previos
 
| Herramienta | Versión mínima |
|---|---|
| Android Studio | Ladybug (2024.2) o superior |
| JDK | 11 |
| Kotlin | 2.0.21 |
| AGP (Android Gradle Plugin) | 8.13.2 |
| Android SDK — `compileSdk` | 36 |
| Android SDK — `minSdk` | 29 (Android 10) |
| Android SDK — `targetSdk` | 36 |
| Mockoon (para modo servidor real) | Cualquier versión reciente |
 
> **Dispositivo / emulador:** Android 10 (API 29) o superior.
 
---
 
## Instalación y configuración
 
### 1. Clonar el repositorio
 
```bash
git clone https://github.com/<org>/ib2026franciscopg.git
cd ib2026franciscopg
```
 
### 2. Configurar Firebase
 
El proyecto requiere un archivo `google-services.json` válido vinculado a tu proyecto Firebase.
 
1. Accede a la [consola de Firebase](https://console.firebase.google.com/) y abre (o crea) el proyecto correspondiente.
2. Descarga el `google-services.json` desde **Configuración del proyecto → Tus apps → Android**.
3. Cópialo en `app/google-services.json` (ya existe un placeholder en el repositorio; sustitúyelo).
### 3. Levantar Mockoon (modo servidor real)
 
El modo de datos real apunta a `https://localhost:3001/`. Para activarlo:
 
1. Instala [Mockoon](https://mockoon.com/).
2. Importa el entorno incluido en el proyecto: `app/src/main/res/raw/mockoon_iberdrola.json`.
3. Arranca el servidor en el puerto **3001**.
4. En la app, activa el toggle **"Servidor real"** desde la pantalla principal.
> Si usas un emulador, `localhost` resuelve directamente al host. En dispositivo físico puede ser necesario usar la IP de tu máquina y ajustar `network_security_config.xml`.
 
### 4. Abrir y compilar
 
Abre el proyecto con Android Studio, sincroniza Gradle y ejecuta la app en un emulador o dispositivo con API ≥ 29.
 
---
 
## Estructura de módulos
 
El proyecto sigue una arquitectura **Clean Architecture multi-módulo**. Cada módulo tiene una responsabilidad única y solo depende hacia las capas internas:
 
```
:app
 └── punto de entrada, DI raíz (Hilt), AndroidManifest principal
 
:presentation
 └── UI (Jetpack Compose, Material 3), ViewModels, navegación
     Depende de: :domain
 
:domain
 └── modelos de negocio, repositorios (interfaces), use cases, abstracciones de analytics/crash
     Sin dependencias de Android Framework
 
:data
 └── implementaciones de repositorios, Retrofit, RetroMock, Room, Firebase
     Depende de: :domain
```
 
### Dependencias entre módulos
 
```
:app ──► :presentation ──► :domain ◄── :data
  └─────────────────────────────────────────┘
```
 
`:domain` no conoce ni a `:data` ni a `:presentation`; la inversión de dependencias se gestiona vía Hilt en `:app`.
 
---
 
## Diagrama de arquitectura
 
```
┌─────────────────────────────────────────────────────────┐
│                        :presentation                    │
│  Compose UI → ViewModel → UseCase (domain)              │
│  UiState / UiEvent  │  StateFlow / SharedFlow           │
└───────────────────────────┬─────────────────────────────┘
                            │ llama a
┌───────────────────────────▼─────────────────────────────┐
│                          :domain                        │
│  UseCase · Repository (interfaz) · Model · Analytics    │
└──────────┬──────────────────────────────────────────────┘
           │ implementado en
┌──────────▼──────────────────────────────────────────────┐
│                           :data                         │
│  Retrofit / RetroMock · Room · Firebase SDK             │
│  RepositoryImpl · Mappers · DTO                         │
└─────────────────────────────────────────────────────────┘
```
 
**Fuente de datos intercambiable en caliente:**
 
```
NetworkModule
    ├── RetroMock  (JSON local en assets/)   ← modo desarrollo
    └── Retrofit   (Mockoon localhost:3001)  ← modo servidor real
```
 
El toggle en `MainScreen` llama a `SetMockUseCase`, que actualiza el flag en `ConfigurationRepository`; Hilt reinyecta la dependencia en el siguiente ciclo.
 
---
