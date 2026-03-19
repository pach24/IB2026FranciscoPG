# IB2026FranciscoPG

Aplicacion nativa Android construida con **Jetpack Compose** para la gestion y visualizacion de facturas energeticas, con cambio de fuente de datos en tiempo real y arquitectura modular.

> **Estado del proyecto:** v1.0.0 – Marzo 2026

**Clean Architecture · MVVM · Multi-Modulo · UI Reactiva**

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Hilt](https://img.shields.io/badge/Hilt-00BFA5?style=for-the-badge&logo=google&logoColor=white)
![Retrofit](https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge)

---

## Descripcion general

Aplicacion que implementa una arquitectura **Clean Architecture de tres capas** desacopladas, permitiendo que la logica de negocio opere de forma independiente al framework de UI o la fuente de datos.

El diferenciador clave es la **flexibilidad de datos**: mediante inyeccion de dependencias, el usuario puede alternar entre un entorno local (RetroMock) y un servidor real (Retrofit + Mockoon) con un solo tap, ideal para demos tecnicas y entornos de desarrollo aislados.

---

## Galeria visual

| **Home y Toggle** | **Lista de facturas** | **Modo oscuro** | **Carga (Skeleton)** |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/dcf1a14e-8e42-484e-8a96-6494a95260fd" width="200" alt="Home Screen"> | <img src="https://github.com/user-attachments/assets/a28f8004-6db8-41c2-8fcc-19d075e1b6b6" width="200" alt="Invoice List"> | <img src="https://github.com/user-attachments/assets/9184baaf-bded-4ed5-a37c-647225b8b08c" width="200" alt="Details"> | <img src="https://github.com/user-attachments/assets/0c46c50d-5581-4269-a2a9-f8d200f2f71e" width="200" alt="Shimmer"> |
| *Dashboard con switch RetroMock/Retrofit.* | *Agrupadas por tipo (Luz/Gas) y estado.* | *Vista en modo oscuro.* | *Carga fluida con placeholders Shimmer.* |

---

## Funcionalidades principales

- **Doble fuente de datos:** Cambio instantaneo entre `Retrofit` (API real via Mockoon) y `RetroMock` (JSON local) sin reiniciar la app.
- **Gestion inteligente de facturas:** Listado optimizado con agrupacion por categoria (Luz/Gas), ordenacion cronologica y destacado visual de la ultima factura.
- **UI/UX moderna:**
  - Construida integramente con **Material 3 Expressive**.
  - **Pull-to-refresh** con indicadores de carga expresivos en todos los estados (datos, vacio, error).
  - **Skeleton Loading** con Shimmer para percepcion de carga instantanea.
  - Auto-switch de tab inteligente (si un tipo de suministro esta vacio, navega al que tiene datos).
- **Estados de error diferenciados:** Error de servidor y error de conexion con iconografia y mensajes distintos, boton de reintento y pull-to-refresh.

---

## Arquitectura

El proyecto sigue los principios **SOLID** y las recomendaciones oficiales de Google para aplicaciones robustas.

### Estructura de modulos

```
IB2026FranciscoPG/
├── :app (Capa de Presentacion)
│   ├── di/              # Modulos Hilt (AppModule, inyeccion de dependencias)
│   ├── core/            # Utilidades transversales (ErrorClassifier)
│   └── presentation/
│       ├── home/        # MainActivity, MainScreen, MainViewModel
│       ├── invoices/    # Pantallas, componentes, ViewModel y mapper de facturas
│       └── theme/       # Tokens de diseno (Spacing, TextSize, Radius...)
├── :domain (Capa de Negocio)
│   ├── model/           # Entidades de dominio (Invoice, SupplyType, InvoiceStatus)
│   ├── repository/      # Contratos (interfaces)
│   └── usecase/         # Casos de uso (GetInvoices, SortInvoices)
└── :data (Capa de Datos)
    ├── local/           # Room (InvoiceDao, InvoiceEntity)
    ├── model/           # DTOs (InvoiceDto, mappers)
    ├── network/         # Retrofit + Retromock (InvoiceApiService)
    └── repository/      # Implementaciones de repositorio
```

1. **`:app` (Presentacion):** UI en Compose, ViewModels con `StateFlow`, y configuracion Hilt. Implementa **UDF (Unidirectional Data Flow)**.
2. **`:domain` (Negocio):** Modulo Kotlin puro sin dependencias Android. Define entidades, enums de dominio (`SupplyType`, `InvoiceStatus`), contratos de repositorio y casos de uso.
3. **`:data` (Datos):** Implementaciones de repositorio, DTOs, servicios Retrofit/Retromock, cache con Room y logica de seleccion de fuente de datos.

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
> 📂 **Ruta:** `app/src/main/assets/InvoicesMockoonEnvironment.json`
>
> **Instrucciones rápidas:**
> 1. Abre **Mockoon**.
> 2. `File` > `Open environment` (o `Ctrl+O`).
> 3. Selecciona el archivo en la ruta indicada arriba.
> 4. Asegúrate de que el servidor esté en **Play** sobre el puerto `3001`.



### 3. Configuracion de red

La app detecta automaticamente si se ejecuta en emulador o dispositivo fisico:

| Entorno | URL base | Notas |
|---|---|---|
| **Emulador AVD** | `http://10.0.2.2:3001/` | `10.0.2.2` es el alias del host en el emulador de Android |
| **Dispositivo fisico** | `http://localhost:3001/` | Requiere redireccion de puerto antes de cada ejecucion |

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
- **Retrofit (Mockoon):** Llamadas HTTP reales contra el servidor Mockoon local. Requiere que Mockoon este en ejecucion.

---

## Modos de datos en detalle

### RetroMock (mock local)

- Usa el fichero `invoices_mock.json` incluido en `assets/`.
- Simula latencia aleatoria (1-3 segundos) para reproducir condiciones reales.
- No requiere configuracion de red.
- Ideal para desarrollo rapido y pruebas offline.

### Retrofit + Mockoon (servidor local)

- Llamadas HTTP reales contra los endpoints configurados en Mockoon.
- Los datos se cachean en **Room** para acceso offline posterior.
- Si la API falla pero hay cache disponible, la app muestra los datos cacheados en lugar de un error.
- Permite modificar las respuestas en Mockoon en tiempo real para probar distintos escenarios (facturas vacias, errores de servidor, etc.).

---

