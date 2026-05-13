# IB2026FranciscoPG

Aplicacion nativa Android construida con **Jetpack Compose** para la gestion y visualizacion de facturas energeticas, con cambio de fuente de datos en tiempo real y arquitectura modular.

> Mayo 2026 (Entrega 3)

**Clean Architecture · MVVM · Multi-Módulo (`:app`, `:presentation`, `:domain`, `:data`) · UI Reactiva**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge)

---

## Descripcion general

Aplicacion que implementa una arquitectura **Clean Architecture multi-módulo** (`:app`, `:presentation`, `:domain`, `:data`), permitiendo que la logica de negocio opere de forma independiente al framework de UI o la fuente de datos.

El diferenciador clave es la **flexibilidad de datos**: mediante inyeccion de dependencias, el usuario puede alternar entre un entorno local (RetroMock) y un servidor real (Retrofit + Mockoon) con un solo tap, ideal para demos tecnicas y entornos de desarrollo aislados.

---

# 🆕 What’s New – Entrega 3

Entrega centrada en el **flujo completo de Factura Electrónica** y un refactor arquitectónico que separa la presentación en su propio módulo.

### 📄 Flujo de Factura Electrónica

- Nueva pantalla **Factura Electrónica** accesible desde Home, con listado de contratos por suministro (**Luz / Gas**) y su estado (**Activo / Inactivo**).
- **Wizard de activación** de 4 pasos (`HorizontalPager` con scroll deshabilitado): selección de contrato → email + consentimiento legal → verificación SMS (6 dígitos) → confirmación de éxito.
- **Wizard de modificación de email** paralelo para contratos ya activos, con la misma mecánica de validación y verificación.
- **Pantalla de éxito** compartida que cierra ambos wizards con feedback visual claro.
- Validaciones en tiempo real: `EmailValidator` (regex RFC), `ValidateVerificationCodeUseCase` (6 dígitos numéricos), botón *Siguiente* deshabilitado hasta cumplir condiciones.
- Reenvío de SMS con `LoadingOverlay` semitransparente que bloquea interacción y `SuccessBannerSMS` auto-dismissible.

### 🚀 Splash Screen

- Splash inicial con tema dedicado (`Theme.IB2026FranciscoPG.Splash`) que precarga datos antes de navegar a Home.

### 🧱 Refactor arquitectónico — módulo `:presentation`

- Extraída toda la UI de `:app` a un nuevo módulo **`:presentation`**, dejando `:app` como contenedor mínimo (Activity, Hilt, Manifest).
- Re-namespacing de paquetes en `:domain` y `:data` para mantener coherencia (`com.iberdrola.practicas2026.FranciscoPG.*`).

### 🧰 Componentes UI reutilizables

- `StepProgressBar` (barra animada del wizard), `StepBottomButtonBar` (Back/Next), `CloseTopBar`, `OtpInput` (entrada de 6 dígitos), `RoundedCheckbox`, `GenericBanner`, `ConfirmDialog`, `InfoDialog`, `UnavailableBanner` (refactorizado a `common`).

### 🌐 Mockoon ahora con HTTPS

- Entorno Mockoon trasladado de `assets/InvoicesMockoonEnvironment.json` a `res/raw/mockoon_iberdrola.json`, ampliado con endpoints de contratos.
- Soporte de **TLS** mediante certificado auto-firmado (`res/raw/certificado.crt` + `clave.key`) y `network_security_config.xml` con `<trust-anchors>`.

### 🗃️ Backend de contratos (mockeado de extremo a extremo)

- Nuevo `ContractApiService` y `ContractDao`/`ContractEntity` (Room) con la misma forma que el resto del stack, **pero hoy todo el flujo es mock**:
  - **Modo RetroMock:** lee `assets/contracts_mock.json` mediante la anotación `@Mock` de RetroMock (sin llamada HTTP real).
  - **Modo Retrofit:** Mockoon **no expone aún** la ruta `contracts.json`, así que `ContractRepositoryImpl` devuelve una **semilla hardcodeada** dentro del propio repositorio cuando el modo mock está desactivado.
  - **`updateEmail`** persiste sólo en Room (`ContractDao.updateEmail`); no hay PUT/PATCH contra Mockoon.
- El andamiaje (DTOs, `ApiResponse`, cache en Room) queda preparado para conectar la API real en una entrega futura sin tocar UI ni dominio.

---

## Galería visual

### <img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Telegram-Animated-Emojis/main/Symbols/Dizzy.webp" alt="Dizzy" width="25" height="25" /> Novedades – Entrega 3

> Flujo de Factura Electrónica, y componentes UX comunes

| Pantalla de contratos | Wizard — Email + legal | Wizard — Código OTP |
|:---------------------:|:----------------------:|:-------------------:|
| <img src="https://github.com/user-attachments/assets/5b2e9394-bbe3-4047-8796-4b7b11cd16c1" width="200" alt="Contratos"/> | <img src="https://github.com/user-attachments/assets/eac865d5-31c4-430e-a335-db550770d035" width="200" alt="Email + legal"/> | <img src="https://github.com/user-attachments/assets/3f230f25-e626-4104-8e67-defc8c5e79f8" width="200" alt="OTP"/> |

| Pantalla de éxito (modificar) | Modificar email | Pantalla de éxito (activar) |
|:-----------------:|:---------------:|:-----------------:|
| <img src="https://github.com/user-attachments/assets/e7fe35d5-6a40-4eb0-a1b0-2d87f76f803e" width="200" alt="Éxito"/> | <img src="https://github.com/user-attachments/assets/5b67a719-9356-4120-afd8-b5fa30869265" width="200" alt="Modificar email"/> | <img src="https://github.com/user-attachments/assets/ee33c344-49d9-4f3c-a377-9afc72cb6825" width="200" alt="Extra"/> |

---

<details>
<summary><strong>📱 Pantallas principales</strong></summary>

<br>

| Home y Toggle | Lista de facturas | Carga (Skeleton) | Bottom Sheet |
|:-------------:|:-----------------:|:----------------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/7d5016e4-bb5d-49a1-bf66-2efb6a4ef19d" width="200"/> | <img src="https://github.com/user-attachments/assets/700552ba-a6e1-4fb5-acd8-2bb5809a8daf" width="200"/> | <img src="https://github.com/user-attachments/assets/72041eaa-bfa3-497b-ac73-c1ddcd86d82a" width="200"/> | <img src="https://github.com/user-attachments/assets/f05b74c3-7565-4396-af56-73417e326141" width="200"/> |

</details>

---

<details>
<summary><strong>🎨 Filtros y estados</strong></summary>

<br>

> Nuevas funcionalidades centradas en filtrado, estados y experiencia de usuario

| Pantalla de filtros | Modo oscuro | Lista filtrada | EmptyState |
|:-------------:|:-----------------:|:----------------:|:-----------:|
| <img src="https://github.com/user-attachments/assets/a770b86b-d78f-4845-a63c-93a66256d930" width="200"/> | <img src="https://github.com/user-attachments/assets/c7f86169-52ab-4709-bedb-be88341b14e6" width="200"/> | <img src="https://github.com/user-attachments/assets/bfd64d74-c1ea-4128-ae86-ed3500dc9df0" width="200"/> | <img src="https://github.com/user-attachments/assets/9fbe45e5-9ff1-4934-8ed7-3ec06227219d" width="200"/> |

</details>





## Funcionalidades principales

- **Doble fuente de datos:** Cambio instantaneo entre `Retrofit` (API real via Mockoon) y `RetroMock` (JSON local) sin reiniciar la app.
- **Gestion inteligente de facturas:** Listado optimizado con agrupacion por categoria (Luz/Gas), ordenacion cronologica y destacado visual de la ultima factura.
- **UI/UX moderna:**
  - Construida integramente con **Material 3 Expressive**.
  - **Pull-to-refresh** con indicadores de carga expresivos en todos los estados (datos, vacio, error).
  - **Skeleton Loading** con Shimmer para percepcion de carga instantanea.
  - Auto-switch de tab inteligente (si un tipo de suministro esta vacio, navega al que tiene datos).
- **Estados de error diferenciados:** Error de servidor y error de conexion con iconografia y mensajes distintos, boton de reintento y pull-to-refresh.
- **Factura Electrónica:** Gestion de contratos por suministro y wizards de **activacion** y **modificacion de email** (4 pasos cada uno) con validacion en tiempo real, verificacion SMS, loading overlay y banners auto-dismissibles.
- **Splash Screen:** Pantalla de carga inicial con tema dedicado.

---

## Arquitectura

El proyecto sigue los principios **SOLID** y las recomendaciones oficiales de Google para aplicaciones robustas.

### Estructura de modulos

```
IB2026FranciscoPG/
├── :app (Punto de entrada)
│   ├── di/                # Modulos Hilt (AppModule, DatabaseModule, NetworkModule, RepositoryModule)
│   └── DeviceUtils.kt     # Helpers especificos de plataforma
├── :presentation (Capa de Presentacion)
│   ├── core/              # Utilidades transversales (ErrorClassifier)
│   └── presentation/
│       ├── home/                  # MainActivity, MainScreen, SplashScreen, MainViewModel
│       ├── myinvoices/            # Listado, filtros, feedback, detalle de facturas
│       ├── electronicinvoice/     # Listado de contratos + wizards (activar / modificar email) + pantalla de exito
│       ├── common/                # StepProgressBar, OtpInput, banners, dialogos, top bars
│       └── theme/                 # Tokens de diseno (Color, Spacing, Type, Theme)
├── :domain (Capa de Negocio)
│   ├── model/             # Invoice, Contract, ContractStatus, SupplyType, InvoiceStatus, EmailValidator…
│   ├── repository/        # Contratos: InvoiceRepository, ContractRepository, ConfigurationRepository, FeedbackRepository
│   └── usecase/           # GetInvoices, FilterInvoices, GetContracts, ValidateEmail, ValidateVerificationCode, ResendCode, UpdateContractEmail, CensorEmail…
└── :data (Capa de Datos)
    ├── local/             # Room: AppDatabase, InvoiceDao/Entity, ContractDao/Entity
    ├── model/             # DTOs (InvoiceDto, ContractDto, ApiEnvelopeDto, ApiResponse) y mappers
    ├── network/           # Retrofit + Retromock: InvoiceApiService, ContractApiService, SafeAwait
    └── repository/        # InvoiceRepositoryImpl, ContractRepositoryImpl, ConfigurationRepositoryImpl, FeedbackRepositoryImpl
```

1. **`:app`:** Punto de entrada minimo. `MainActivity`, configuracion Hilt y `AndroidManifest`. No contiene UI.
2. **`:presentation`:** UI en Compose, ViewModels con `StateFlow`, navegacion, splash y todos los componentes reutilizables. Implementa **UDF (Unidirectional Data Flow)**.
3. **`:domain`:** Modulo Kotlin puro sin dependencias Android. Entidades, enums, validators, contratos de repositorio y casos de uso.
4. **`:data`:** Implementaciones de repositorio, DTOs, servicios Retrofit/Retromock, cache con Room y seleccion de fuente de datos.

---

## Stack tecnologico

| Categoria | Tecnologia |
|---|---|
| **Lenguaje** | Kotlin + Coroutines & Flow |
| **UI** | Jetpack Compose (Material 3 Expressive) |
| **Inyeccion de dependencias** | Hilt (Dagger) |
| **Networking** | Retrofit + OkHttp + Retromock |
| **Base de datos local** | Room |
| **Navegacion** | Navigation Compose |
| **Gestion de estado** | MVVM con `StateFlow` + `collectAsStateWithLifecycle()` |
| **Mock server** | Mockoon (servidor local REST) |

---

## Instalacion y puesta en marcha

### Requisitos previos

- Android Studio Giraffe o superior
- JDK 11+
- Min SDK: 29
- [Mockoon](https://mockoon.com/) instalado (para el modo Retrofit)

### 1. Clonar el repositorio

```bash
git clone https://github.com/pach24/IB2026FranciscoPG.git
cd IB2026FranciscoPG
```

### 2. Configurar Mockoon (modo Retrofit)

> [!WARNING]
> ### ⚠️ ARCHIVO DE ENTORNO REQUERIDO
> Para que el modo **Retrofit** funcione, debes importar manualmente el siguiente archivo en tu aplicación **Mockoon**:
>
> 📂 **Ruta:** `app/src/main/res/raw/mockoon_iberdrola.json`
>
> **Instrucciones rápidas:**
> 1. Abre **Mockoon**.
> 2. `File` > `Open environment` (o `Ctrl+O`).
> 3. Selecciona el archivo en la ruta indicada arriba.
> 4. El entorno está configurado para servir por **HTTPS** con un certificado auto-firmado (`certificado.crt` + `clave.key` en `app/src/main/res/raw/`). Mockoon los espera en su directorio de trabajo: copia ambos junto al `.json` o ajusta `certPath` / `keyPath` desde la pestaña *TLS* de Mockoon.
> 5. Asegúrate de que el servidor esté en **Play** sobre el puerto `3001`.
>
> El cert auto-firmado ya está confiado por la app vía `network_security_config.xml` (`<trust-anchors>` → `@raw/certificado`). Si lo regeneras, sustituye el archivo en `res/raw/`.



### 3. Configuracion de red

La app detecta automaticamente si se ejecuta en emulador o dispositivo fisico:

| Entorno | URL base | Notas |
|---|---|---|
| **Emulador AVD** | `https://10.0.2.2:3001/` | `10.0.2.2` es el alias del host en el emulador de Android |
| **Dispositivo fisico** | `https://localhost:3001/` | Requiere redireccion de puerto antes de cada ejecucion |

#### Dispositivo fisico: redireccion de puerto

Antes de ejecutar la app en un dispositivo fisico conectado por USB, ejecutar:

```bash
adb reverse tcp:3001 tcp:3001
```

> Este comando redirige el puerto 3001 del dispositivo al puerto 3001 del equipo donde corre Mockoon. Hay que repetirlo cada vez que se reconecta el dispositivo.

### 4. Ejecutar la app

1. Sincronizar Gradle en Android Studio.
2. Seleccionar el modulo `:app` como configuracion de ejecucion.
3. Lanzar en emulador o dispositivo.

### 5. Alternar modo de datos

En la pantalla principal, el switch en la esquina inferior derecha permite alternar entre:

- **RetroMock:** Datos simulados desde un JSON local embebido en la app. No requiere Mockoon ni conexion de red.
- **Retrofit (Mockoon):** Llamadas HTTPS reales contra el servidor Mockoon local (TLS con certificado auto-firmado). Requiere que Mockoon este en ejecucion.

---
