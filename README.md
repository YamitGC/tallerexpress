# Taller Express

Sistema de gestión para talleres mecánicos, desarrollado en **Java** con arquitectura en capas (presentación, controlador, servicio, repositorio y dominio) y persistencia en **PostgreSQL** mediante JDBC.

## Descripción general

Taller Express permite administrar el flujo completo de un taller automotriz:

- **Usuarios**: autenticación (login) y registro de recepcionistas mediante el patrón *Decorator* (`UserRegistrationDecorator`), que asigna automáticamente rol, estado activo y fecha de creación.
- **Clientes y Vehículos**: registro de clientes, asociación de vehículos y consulta del historial vehicular por cliente.
- **Repuestos**: alta, actualización, listado y filtrado por categoría o proveedor, con control de stock.
- **Órdenes de Servicio**: apertura de órdenes con diagnóstico, mano de obra y repuestos asociados; cambio de estado (`EN_ESPERA`, `ACTIVO`, `INACTIVO`); consulta de historial técnico por placa; y cálculo del costo total a cobrar (mano de obra + repuestos).

La interfaz de usuario se implementa con **Swing** utilizando cuadros de diálogo (`JOptionPane`), y la capa de acceso a datos utiliza `PreparedStatement` con `RETURN_GENERATED_KEYS` para persistencia en PostgreSQL.

### Arquitectura por capas

```
presentation/  → Vistas con JOptionPane (interacción con el usuario)
controller/    → Orquesta llamadas a servicios y estandariza respuestas (Respuesta<T>)
service/       → Reglas de negocio (interfaces + implementaciones en /impl)
repository/    → Contratos de acceso a datos (interfaces + implementaciones JDBC en /jdbc)
domain/
  models/      → Entidades con validaciones internas (Cliente, Vehiculo, Repuesto, User, OrdenDeServicio, DetalleOrdenRepuesto)
  enums/       → Estado, Roles
  exceptions/  → DatosInvalidosException, EntidadDuplicadaException, EntidadNoEncontradaException, ReglaNegocioException
```

## Requisitos previos

| Herramienta | Versión mínima | Notas |
|---|---|---|
| **Java (JDK)** | 21 | Definido en `pom.xml` (`maven.compiler.release`) |
| **Maven** | 3.8+ | Gestión de dependencias y build |
| **PostgreSQL** | 12+ | Motor de base de datos |
| **Driver JDBC** | `org.postgresql:postgresql:42.7.2` | Se descarga automáticamente vía Maven |

## Configuración de la base de datos

1. Crea la base de datos:

   ```sql
   CREATE DATABASE taller_db;
   ```

2. Crea las tablas principales (ajusta tipos/nombres si tu esquema difiere):

   ```sql
   CREATE TABLE users (
       id SERIAL PRIMARY KEY,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       nombre_completo VARCHAR(150) NOT NULL,
       correo VARCHAR(150) NOT NULL,
       rol VARCHAR(20) NOT NULL,           -- 'ADMIN' | 'RECEPCIONISTA'
       is_activo BOOLEAN NOT NULL DEFAULT TRUE,
       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE clientes (
       id SERIAL PRIMARY KEY,
       numero_identificacion VARCHAR(30) NOT NULL,
       nombre_completo VARCHAR(150) NOT NULL,
       telefono VARCHAR(30) NOT NULL,
       correo VARCHAR(150) NOT NULL,
       direccion VARCHAR(200) NOT NULL,
       estado VARCHAR(20) NOT NULL,        -- 'EN_ESPERA' | 'ACTIVO' | 'INACTIVO'
       fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE vehiculos (
       id SERIAL PRIMARY KEY,
       cliente_id INTEGER NOT NULL REFERENCES clientes(id),
       placa VARCHAR(15) UNIQUE NOT NULL,
       marca VARCHAR(50) NOT NULL,
       modelo VARCHAR(50) NOT NULL,
       categoria VARCHAR(50) NOT NULL,
       anio_modelo INTEGER NOT NULL
   );

   CREATE TABLE repuestos (
       id SERIAL PRIMARY KEY,
       codigo_referencia VARCHAR(30) NOT NULL,
       nombre VARCHAR(100) NOT NULL,
       categoria VARCHAR(50) NOT NULL,
       proveedor VARCHAR(100) NOT NULL,
       stock_total BIGINT NOT NULL,
       stock_disponible BIGINT NOT NULL,
       precio_unitario NUMERIC(12,2) NOT NULL,
       is_activo BOOLEAN NOT NULL DEFAULT TRUE,
       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE ordenes_servicio (
       id SERIAL PRIMARY KEY,
       vehiculo_id INTEGER NOT NULL REFERENCES vehiculos(id),
       cliente_id INTEGER NOT NULL REFERENCES clientes(id),
       user_id INTEGER NOT NULL REFERENCES users(id),
       descripcion_falla TEXT NOT NULL,
       diagnostico TEXT NOT NULL,
       total_mano_obra NUMERIC(12,2) NOT NULL,
       total_repuestos NUMERIC(12,2) NOT NULL,
       total_pagar NUMERIC(12,2) NOT NULL,
       estado VARCHAR(20) NOT NULL,
       fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE detalle_orden_repuestos (
       id SERIAL PRIMARY KEY,
       orden_servicio_id INTEGER NOT NULL REFERENCES ordenes_servicio(id),
       repuesto_id INTEGER NOT NULL REFERENCES repuestos(id),
       precio_unitario_historico NUMERIC(12,2) NOT NULL
   );
   ```

3. Crea un usuario administrador inicial para poder iniciar sesión:

   ```sql
   INSERT INTO users (username, password, nombre_completo, correo, rol, is_activo, created_at)
   VALUES ('admin_taller', '1234', 'Administrador', 'admin@tallerexpress.com', 'ADMIN', TRUE, NOW());
   ```

4. Actualiza las credenciales de conexión en `TallerExpress.java` si es necesario:

   ```java
   String url = "jdbc:postgresql://localhost:5434/taller_db";
   String userDB = "postgres";
   String passDB = "yamitgc01";
   ```

## Pasos de configuración y ejecución

1. **Clonar el repositorio**

   ```bash
   git clone https://github.com/YamitGC/tallerexpress
   cd tallerexpress
   ```

2. **Compilar el proyecto con Maven**

   ```bash
   mvn clean compile
   ```

3. **Verificar/crear la base de datos** siguiendo la sección anterior.

4. **Ejecutar la aplicación**

   Desde NetBeans: abrir el proyecto y ejecutar `TallerExpress.java` (clase `main`).

   Desde línea de comandos con Maven:

   ```bash
   mvn exec:java -Dexec.mainClass="com.mycompany.tallerexpress.TallerExpress"
   ```

5. **Iniciar sesión** con las credenciales creadas en el paso de base de datos.

6. Navegar por el **Panel de Control** entre los módulos: Repuestos, Clientes y Vehículos, Usuarios, Órdenes de Servicio.

## Capturas de pantalla (JOptionPane)

> Las siguientes capturas deben tomarse ejecutando la aplicación y guardarse en una carpeta `docs/screenshots/` del repositorio. Reemplaza las rutas de ejemplo por tus imágenes reales.

| Pantalla | Descripción | Imagen |
|---|---|---|
| Login | Autenticación de usuario (`ejecutarLogin`) | `docs/screenshots/login.png` |
| Panel de Control | Menú principal de módulos | `docs/screenshots/panel-control.png` |
| Registrar Cliente | Formulario de alta de cliente | `docs/screenshots/registrar-cliente.png` |
| Registrar Vehículo | Formulario de alta de vehículo | `docs/screenshots/registrar-vehiculo.png` |
| Historial Vehicular | Tabla de vehículos por cliente | `docs/screenshots/historial-vehiculos.png` |
| Registrar Repuesto | Formulario de alta de repuesto | `docs/screenshots/registrar-repuesto.png` |
| Listado de Repuestos | Tabla con filtros por categoría/proveedor | `docs/screenshots/listado-repuestos.png` |
| Crear Orden de Servicio | Formulario de apertura de orden | `docs/screenshots/crear-orden.png` |
| Cambiar Estado de Orden | Selector de nuevo estado | `docs/screenshots/cambiar-estado.png` |
| Liquidación Financiera | Cálculo del total a cobrar | `docs/screenshots/liquidacion.png` |

Ejemplo de inserción de imagen en Markdown:

```markdown
![Login](docs/screenshots/login.png)
```

## Diagrama de clases

```mermaid
classDiagram
    class Cliente {
        -Long id
        -String numeroIdentificacion
        -String nombreCompleto
        -String telefono
        -String correo
        -String direccion
        -Estado estado
        -OffsetDateTime fechaRegistro
    }

    class Vehiculo {
        -Long id
        -Cliente cliente
        -String placa
        -String marca
        -String modelo
        -String categoria
        -int anioModelo
    }

    class Repuesto {
        -Long id
        -String codigoReferencia
        -String nombre
        -String categoria
        -String proveedor
        -Long stockTotal
        -Long stockDisponible
        -BigDecimal precioUnitario
        -boolean isActivo
        -OffsetDateTime createdAt
    }

    class User {
        -Long id
        -String username
        -String password
        -String nombreCompleto
        -String correo
        -Roles rol
        -boolean isActivo
        -OffsetDateTime createdAt
    }

    class OrdenDeServicio {
        -Long id
        -Vehiculo vehiculo
        -Cliente cliente
        -User user
        -String descripcionFalla
        -String diagnostico
        -BigDecimal totalManoObra
        -BigDecimal totalRepuestos
        -BigDecimal totalPagar
        -Estado estado
        -OffsetDateTime fechaRegistro
    }

    class DetalleOrdenRepuesto {
        -Long id
        -OrdenDeServicio ordenDeServicio
        -Repuesto repuesto
        -BigDecimal precioUnitarioHistorico
    }

    class Estado {
        <<enumeration>>
        EN_ESPERA
        ACTIVO
        INACTIVO
    }

    class Roles {
        <<enumeration>>
        ADMIN
        RECEPCIONISTA
    }

    Cliente "1" --> "0..*" Vehiculo : posee
    Cliente "1" --> "0..*" OrdenDeServicio : solicita
    Vehiculo "1" --> "0..*" OrdenDeServicio : es atendido en
    User "1" --> "0..*" OrdenDeServicio : registra
    OrdenDeServicio "1" --> "0..*" DetalleOrdenRepuesto : incluye
    Repuesto "1" --> "0..*" DetalleOrdenRepuesto : usado en
    Cliente ..> Estado
    User ..> Roles
    OrdenDeServicio ..> Estado

    class RepuestoService {
        <<interface>>
        +registrar(Repuesto) Repuesto
        +actualizar(Repuesto) void
        +listarTodos() List~Repuesto~
        +buscarPorCategoria(String) List~Repuesto~
        +buscarPorProveedor(String) List~Repuesto~
    }

    class Cliente_VehiculoService {
        <<interface>>
        +registrarCliente(Cliente) Cliente
        +registrarVehiculo(Vehiculo) Vehiculo
        +consultarHistorialVehiculosPorCliente(Long) List~Vehiculo~
    }

    class OrdenDeServicioService {
        <<interface>>
        +registrarOrden(OrdenDeServicio, List) OrdenDeServicio
        +actualizarEstado(Long, Estado) void
        +consultarHistorialPorVehiculo(String) List~OrdenDeServicio~
        +calcularCostoTotal(Long) BigDecimal
    }

    class UserService {
        <<interface>>
        +login(String, String) User
        +registrar(User) User
        +listarTodos() List~User~
    }

    class UserServiceImpl
    class UserRegistrationDecorator

    UserService <|.. UserServiceImpl
    UserService <|.. UserRegistrationDecorator
    UserRegistrationDecorator --> UserService : decora

    RepuestoService <|.. RepuestoServiceImpl
    Cliente_VehiculoService <|.. Cliente_VehiculoServiceImpl
    OrdenDeServicioService <|.. OrdenDeServicioServiceImpl
```

## Diagrama de casos de uso

```mermaid
flowchart TB
    Admin([Administrador])
    Recep([Recepcionista])

    subgraph Sistema["Taller Express"]
        UC1((Iniciar sesión))
        UC2((Registrar recepcionista))
        UC3((Listar usuarios))
        UC4((Registrar cliente))
        UC5((Registrar vehículo))
        UC6((Consultar historial de vehículos))
        UC7((Registrar repuesto))
        UC8((Actualizar repuesto))
        UC9((Listar / filtrar repuestos))
        UC10((Abrir orden de servicio))
        UC11((Cambiar estado de orden))
        UC12((Consultar historial por placa))
        UC13((Calcular costo total))
    end

    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC13

    Recep --> UC1
    Recep --> UC4
    Recep --> UC5
    Recep --> UC6
    Recep --> UC9
    Recep --> UC10
    Recep --> UC11
    Recep --> UC12
    Recep --> UC13
```

> **Nota:** los diagramas están escritos en sintaxis [Mermaid](https://mermaid.js.org/) y se renderizan automáticamente en GitHub, GitLab y la mayoría de visores de Markdown. Si tu visor no los soporta, puedes generarlos como imagen con [mermaid.live](https://mermaid.live/) y reemplazar los bloques de código por `![diagrama](docs/diagramas/clases.png)`.

## Estructura del proyecto

```
src/main/java/com/mycompany/tallerexpress/
├── config/            # Configuración de base de datos
├── controller/        # Controladores (orquestan servicios, retornan Respuesta<T>)
├── domain/
│   ├── enums/          # Estado, Roles
│   ├── exceptions/      # Excepciones de negocio y validación
│   └── models/          # Entidades del dominio
├── presentation/       # Vistas Swing (JOptionPane)
├── repository/         # Interfaces de repositorio
│   └── jdbc/            # Implementaciones JDBC (PostgreSQL)
├── service/             # Interfaces de servicio
│   └── impl/             # Implementaciones + Decorator de registro de usuarios
└── TallerExpress.java   # Punto de entrada / composición de dependencias
```

## Tecnologías utilizadas

- Java 21
- Maven
- PostgreSQL + JDBC (`postgresql:42.7.2`)
- Swing (`JOptionPane`) para la interfaz de usuario
- Patrones de diseño: **Decorator** (registro de usuarios con valores por defecto), capas repositorio/servicio/controlador/presentación# Taller Express

Sistema de gestión para talleres mecánicos, desarrollado en **Java** con arquitectura en capas (presentación, controlador, servicio, repositorio y dominio) y persistencia en **PostgreSQL** mediante JDBC.

## Descripción general

Taller Express permite administrar el flujo completo de un taller automotriz:

- **Usuarios**: autenticación (login) y registro de recepcionistas mediante el patrón *Decorator* (`UserRegistrationDecorator`), que asigna automáticamente rol, estado activo y fecha de creación.
- **Clientes y Vehículos**: registro de clientes, asociación de vehículos y consulta del historial vehicular por cliente.
- **Repuestos**: alta, actualización, listado y filtrado por categoría o proveedor, con control de stock.
- **Órdenes de Servicio**: apertura de órdenes con diagnóstico, mano de obra y repuestos asociados; cambio de estado (`EN_ESPERA`, `ACTIVO`, `INACTIVO`); consulta de historial técnico por placa; y cálculo del costo total a cobrar (mano de obra + repuestos).

La interfaz de usuario se implementa con **Swing** utilizando cuadros de diálogo (`JOptionPane`), y la capa de acceso a datos utiliza `PreparedStatement` con `RETURN_GENERATED_KEYS` para persistencia en PostgreSQL.

### Arquitectura por capas

```
presentation/  → Vistas con JOptionPane (interacción con el usuario)
controller/    → Orquesta llamadas a servicios y estandariza respuestas (Respuesta<T>)
service/       → Reglas de negocio (interfaces + implementaciones en /impl)
repository/    → Contratos de acceso a datos (interfaces + implementaciones JDBC en /jdbc)
domain/
  models/      → Entidades con validaciones internas (Cliente, Vehiculo, Repuesto, User, OrdenDeServicio, DetalleOrdenRepuesto)
  enums/       → Estado, Roles
  exceptions/  → DatosInvalidosException, EntidadDuplicadaException, EntidadNoEncontradaException, ReglaNegocioException
```

## Requisitos previos

| Herramienta | Versión mínima | Notas |
|---|---|---|
| **Java (JDK)** | 21 | Definido en `pom.xml` (`maven.compiler.release`) |
| **Maven** | 3.8+ | Gestión de dependencias y build |
| **PostgreSQL** | 12+ | Motor de base de datos |
| **Driver JDBC** | `org.postgresql:postgresql:42.7.2` | Se descarga automáticamente vía Maven |

## Configuración de la base de datos

1. Crea la base de datos:

   ```sql
   CREATE DATABASE taller_db;
   ```

2. Crea las tablas principales (ajusta tipos/nombres si tu esquema difiere):

   ```sql
   CREATE TABLE users (
       id SERIAL PRIMARY KEY,
       username VARCHAR(50) UNIQUE NOT NULL,
       password VARCHAR(255) NOT NULL,
       nombre_completo VARCHAR(150) NOT NULL,
       correo VARCHAR(150) NOT NULL,
       rol VARCHAR(20) NOT NULL,           -- 'ADMIN' | 'RECEPCIONISTA'
       is_activo BOOLEAN NOT NULL DEFAULT TRUE,
       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE clientes (
       id SERIAL PRIMARY KEY,
       numero_identificacion VARCHAR(30) NOT NULL,
       nombre_completo VARCHAR(150) NOT NULL,
       telefono VARCHAR(30) NOT NULL,
       correo VARCHAR(150) NOT NULL,
       direccion VARCHAR(200) NOT NULL,
       estado VARCHAR(20) NOT NULL,        -- 'EN_ESPERA' | 'ACTIVO' | 'INACTIVO'
       fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE vehiculos (
       id SERIAL PRIMARY KEY,
       cliente_id INTEGER NOT NULL REFERENCES clientes(id),
       placa VARCHAR(15) UNIQUE NOT NULL,
       marca VARCHAR(50) NOT NULL,
       modelo VARCHAR(50) NOT NULL,
       categoria VARCHAR(50) NOT NULL,
       anio_modelo INTEGER NOT NULL
   );

   CREATE TABLE repuestos (
       id SERIAL PRIMARY KEY,
       codigo_referencia VARCHAR(30) NOT NULL,
       nombre VARCHAR(100) NOT NULL,
       categoria VARCHAR(50) NOT NULL,
       proveedor VARCHAR(100) NOT NULL,
       stock_total BIGINT NOT NULL,
       stock_disponible BIGINT NOT NULL,
       precio_unitario NUMERIC(12,2) NOT NULL,
       is_activo BOOLEAN NOT NULL DEFAULT TRUE,
       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE ordenes_servicio (
       id SERIAL PRIMARY KEY,
       vehiculo_id INTEGER NOT NULL REFERENCES vehiculos(id),
       cliente_id INTEGER NOT NULL REFERENCES clientes(id),
       user_id INTEGER NOT NULL REFERENCES users(id),
       descripcion_falla TEXT NOT NULL,
       diagnostico TEXT NOT NULL,
       total_mano_obra NUMERIC(12,2) NOT NULL,
       total_repuestos NUMERIC(12,2) NOT NULL,
       total_pagar NUMERIC(12,2) NOT NULL,
       estado VARCHAR(20) NOT NULL,
       fecha_registro TIMESTAMPTZ NOT NULL DEFAULT NOW()
   );

   CREATE TABLE detalle_orden_repuestos (
       id SERIAL PRIMARY KEY,
       orden_servicio_id INTEGER NOT NULL REFERENCES ordenes_servicio(id),
       repuesto_id INTEGER NOT NULL REFERENCES repuestos(id),
       precio_unitario_historico NUMERIC(12,2) NOT NULL
   );
   ```

3. Crea un usuario administrador inicial para poder iniciar sesión:

   ```sql
   INSERT INTO users (username, password, nombre_completo, correo, rol, is_activo, created_at)
   VALUES ('admin_taller', '1234', 'Administrador', 'admin@tallerexpress.com', 'ADMIN', TRUE, NOW());
   ```

4. Actualiza las credenciales de conexión en `TallerExpress.java` si es necesario:

   ```java
   String url = "jdbc:postgresql://localhost:5434/taller_db";
   String userDB = "postgres";
   String passDB = "yamitgc01";
   ```

## Pasos de configuración y ejecución

1. **Clonar el repositorio**

   ```bash
   git clone <url-del-repositorio>
   cd TallerExpress
   ```

2. **Compilar el proyecto con Maven**

   ```bash
   mvn clean compile
   ```

3. **Verificar/crear la base de datos** siguiendo la sección anterior.

4. **Ejecutar la aplicación**

   Desde NetBeans: abrir el proyecto y ejecutar `TallerExpress.java` (clase `main`).

   Desde línea de comandos con Maven:

   ```bash
   mvn exec:java -Dexec.mainClass="com.mycompany.tallerexpress.TallerExpress"
   ```

5. **Iniciar sesión** con las credenciales creadas en el paso de base de datos.

6. Navegar por el **Panel de Control** entre los módulos: Repuestos, Clientes y Vehículos, Usuarios, Órdenes de Servicio.

## Capturas de pantalla (JOptionPane)

> Las siguientes capturas deben tomarse ejecutando la aplicación y guardarse en una carpeta `docs/screenshots/` del repositorio. Reemplaza las rutas de ejemplo por tus imágenes reales.

| Pantalla | Descripción | Imagen |
|---|---|---|
| Login | Autenticación de usuario (`ejecutarLogin`) | `docs/screenshots/login.png` |
| Panel de Control | Menú principal de módulos | `docs/screenshots/panel-control.png` |
| Registrar Cliente | Formulario de alta de cliente | `docs/screenshots/registrar-cliente.png` |
| Registrar Vehículo | Formulario de alta de vehículo | `docs/screenshots/registrar-vehiculo.png` |
| Historial Vehicular | Tabla de vehículos por cliente | `docs/screenshots/historial-vehiculos.png` |
| Registrar Repuesto | Formulario de alta de repuesto | `docs/screenshots/registrar-repuesto.png` |
| Listado de Repuestos | Tabla con filtros por categoría/proveedor | `docs/screenshots/listado-repuestos.png` |
| Crear Orden de Servicio | Formulario de apertura de orden | `docs/screenshots/crear-orden.png` |
| Cambiar Estado de Orden | Selector de nuevo estado | `docs/screenshots/cambiar-estado.png` |
| Liquidación Financiera | Cálculo del total a cobrar | `docs/screenshots/liquidacion.png` |

Ejemplo de inserción de imagen en Markdown:

```markdown
![Login](docs/screenshots/login.png)
```

## Diagrama de clases

```mermaid
classDiagram
    class Cliente {
        -Long id
        -String numeroIdentificacion
        -String nombreCompleto
        -String telefono
        -String correo
        -String direccion
        -Estado estado
        -OffsetDateTime fechaRegistro
    }

    class Vehiculo {
        -Long id
        -Cliente cliente
        -String placa
        -String marca
        -String modelo
        -String categoria
        -int anioModelo
    }

    class Repuesto {
        -Long id
        -String codigoReferencia
        -String nombre
        -String categoria
        -String proveedor
        -Long stockTotal
        -Long stockDisponible
        -BigDecimal precioUnitario
        -boolean isActivo
        -OffsetDateTime createdAt
    }

    class User {
        -Long id
        -String username
        -String password
        -String nombreCompleto
        -String correo
        -Roles rol
        -boolean isActivo
        -OffsetDateTime createdAt
    }

    class OrdenDeServicio {
        -Long id
        -Vehiculo vehiculo
        -Cliente cliente
        -User user
        -String descripcionFalla
        -String diagnostico
        -BigDecimal totalManoObra
        -BigDecimal totalRepuestos
        -BigDecimal totalPagar
        -Estado estado
        -OffsetDateTime fechaRegistro
    }

    class DetalleOrdenRepuesto {
        -Long id
        -OrdenDeServicio ordenDeServicio
        -Repuesto repuesto
        -BigDecimal precioUnitarioHistorico
    }

    class Estado {
        <<enumeration>>
        EN_ESPERA
        ACTIVO
        INACTIVO
    }

    class Roles {
        <<enumeration>>
        ADMIN
        RECEPCIONISTA
    }

    Cliente "1" --> "0..*" Vehiculo : posee
    Cliente "1" --> "0..*" OrdenDeServicio : solicita
    Vehiculo "1" --> "0..*" OrdenDeServicio : es atendido en
    User "1" --> "0..*" OrdenDeServicio : registra
    OrdenDeServicio "1" --> "0..*" DetalleOrdenRepuesto : incluye
    Repuesto "1" --> "0..*" DetalleOrdenRepuesto : usado en
    Cliente ..> Estado
    User ..> Roles
    OrdenDeServicio ..> Estado

    class RepuestoService {
        <<interface>>
        +registrar(Repuesto) Repuesto
        +actualizar(Repuesto) void
        +listarTodos() List~Repuesto~
        +buscarPorCategoria(String) List~Repuesto~
        +buscarPorProveedor(String) List~Repuesto~
    }

    class Cliente_VehiculoService {
        <<interface>>
        +registrarCliente(Cliente) Cliente
        +registrarVehiculo(Vehiculo) Vehiculo
        +consultarHistorialVehiculosPorCliente(Long) List~Vehiculo~
    }

    class OrdenDeServicioService {
        <<interface>>
        +registrarOrden(OrdenDeServicio, List) OrdenDeServicio
        +actualizarEstado(Long, Estado) void
        +consultarHistorialPorVehiculo(String) List~OrdenDeServicio~
        +calcularCostoTotal(Long) BigDecimal
    }

    class UserService {
        <<interface>>
        +login(String, String) User
        +registrar(User) User
        +listarTodos() List~User~
    }

    class UserServiceImpl
    class UserRegistrationDecorator

    UserService <|.. UserServiceImpl
    UserService <|.. UserRegistrationDecorator
    UserRegistrationDecorator --> UserService : decora

    RepuestoService <|.. RepuestoServiceImpl
    Cliente_VehiculoService <|.. Cliente_VehiculoServiceImpl
    OrdenDeServicioService <|.. OrdenDeServicioServiceImpl
```

## Diagrama de casos de uso

```mermaid
flowchart TB
    Admin([Administrador])
    Recep([Recepcionista])

    subgraph Sistema["Taller Express"]
        UC1((Iniciar sesión))
        UC2((Registrar recepcionista))
        UC3((Listar usuarios))
        UC4((Registrar cliente))
        UC5((Registrar vehículo))
        UC6((Consultar historial de vehículos))
        UC7((Registrar repuesto))
        UC8((Actualizar repuesto))
        UC9((Listar / filtrar repuestos))
        UC10((Abrir orden de servicio))
        UC11((Cambiar estado de orden))
        UC12((Consultar historial por placa))
        UC13((Calcular costo total))
    end

    Admin --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12
    Admin --> UC13

    Recep --> UC1
    Recep --> UC4
    Recep --> UC5
    Recep --> UC6
    Recep --> UC9
    Recep --> UC10
    Recep --> UC11
    Recep --> UC12
    Recep --> UC13
```

> **Nota:** los diagramas están escritos en sintaxis [Mermaid](https://mermaid.js.org/) y se renderizan automáticamente en GitHub, GitLab y la mayoría de visores de Markdown. Si tu visor no los soporta, puedes generarlos como imagen con [mermaid.live](https://mermaid.live/) y reemplazar los bloques de código por `![diagrama](docs/diagramas/clases.png)`.

## Estructura del proyecto

```
src/main/java/com/mycompany/tallerexpress/
├── config/            # Configuración de base de datos
├── controller/        # Controladores (orquestan servicios, retornan Respuesta<T>)
├── domain/
│   ├── enums/          # Estado, Roles
│   ├── exceptions/      # Excepciones de negocio y validación
│   └── models/          # Entidades del dominio
├── presentation/       # Vistas Swing (JOptionPane)
├── repository/         # Interfaces de repositorio
│   └── jdbc/            # Implementaciones JDBC (PostgreSQL)
├── service/             # Interfaces de servicio
│   └── impl/             # Implementaciones + Decorator de registro de usuarios
└── TallerExpress.java   # Punto de entrada / composición de dependencias
```

## Tecnologías utilizadas

- Java 21
- Maven
- PostgreSQL + JDBC (`postgresql:42.7.2`)
- Swing (`JOptionPane`) para la interfaz de usuario
- Patrones de diseño: **Decorator** (registro de usuarios con valores por defecto), capas repositorio/servicio/controlador/presentaciónV