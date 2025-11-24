# Tienda de Ropa - Sistema de Gestión de Prendas y Colecciones

## Descripción General
Sistema web de gestión de tienda de ropa desarrollado con **Java 17** y **Spring Boot 3.x**. Permite gestionar colecciones de prendas con una relación padre-hijo, incluyendo operaciones CRUD, validaciones y eliminación lógica.

## 🚀 Aplicación en Vivo
**URL de Producción**: [https://tienda-colecciones-docker.onrender.com](https://tienda-colecciones-docker.onrender.com)

*Nota: Al usar el plan gratuito de Render, la aplicación puede tardar 30-60 segundos en cargar la primera vez si ha estado inactiva.*

---

## 1. Nombre del Proyecto

**TiendaColecciones** - Sistema de gestión de prendas y colecciones

Proyecto educativo desarrollado como continuidad en la materia de Desarrollo de Servicios Web.

---

## 2. Requisitos para Desarrollo Local

- **Java 17** (JDK 17 - Adoptium, Oracle, Amazon Corretto)
- **Maven 3.8+** (para compilación y ejecución)
- **MySQL 8.0** (base de datos `tienda_ropa`)
- **(Opcional) Docker** para levantar base de datos rápidamente
- **IDE recomendado**: VS Code, IntelliJ IDEA o Eclipse

### Requisitos Mínimos
- Conexión a internet (para descargar dependencias Maven)
- ~500MB de espacio libre
- Puerto 8080 disponible (puerto por defecto de la aplicación)

---

## 3. Pasos de Instalación

### Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/WilderSantamaria18/TiendaColecciones.git
cd TiendaColecciones
```

### Paso 2: Configurar la Base de Datos

#### Opción A: Crear base de datos manualmente
```sql
CREATE DATABASE IF NOT EXISTS tienda_ropa;
USE tienda_ropa;

CREATE TABLE IF NOT EXISTS colecciones (
  id_coleccion INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  temporada VARCHAR(50),
  anio INT NOT NULL,
  estado TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS prendas (
  id_prenda BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  talla VARCHAR(10),
  color VARCHAR(50),
  precio DECIMAL(10,2) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'disponible',
  id_coleccion INT NOT NULL,
  FOREIGN KEY (id_coleccion) REFERENCES colecciones(id_coleccion)
);
```

#### Opción B: Ejecutar script SQL incluido
```bash
mysql -u root -p < database_script.sql
```

#### Opción C: Levantar MySQL con Docker (recomendado)
```powershell
docker run --name tienda-mysql -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_DATABASE=tienda_ropa -p 3306:3306 -d mysql:8.0
```

### Paso 3: Configurar application.properties

Editar `src/main/resources/application.properties`:

```properties
spring.application.name=Continua3
server.port=${PORT:8080}

# Base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/tienda_ropa
spring.datasource.username=root
spring.datasource.password=123456

# Configuracion JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

**Nota**: Ajusta usuario, contraseña y puerto según tu configuración local.

### Paso 4: Compilar el Proyecto

```powershell
# Compilar y crear JAR
mvn clean package -DskipTests

# Solo compilar sin empaquetar
mvn clean compile -DskipTests
```

Si la compilación es exitosa, deberías ver: `BUILD SUCCESS`

---

## 4. Ejecución

### Opción 1: Ejecutar con Maven (Desarrollo)
```powershell
mvn spring-boot:run
```

### Opción 2: Ejecutar JAR empaquetado
```powershell
mvn clean package -DskipTests
java -jar target\Continua3-0.0.1-SNAPSHOT.jar
```

### Opción 3: Ejecutar con puerto personalizado
```powershell
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Opción 4: Ejecutar desde IDE
1. Abrir proyecto en IntelliJ IDEA o VS Code
2. Configurar SDK a Java 17
3. Hacer clic derecho en `Continua3Application.java` → **Run**

### Acceso a la Aplicación
Una vez ejecutada, acceder en el navegador:
- **Página principal**: `http://localhost:8080/`
- **Listado de prendas**: `http://localhost:8080/web/prendas/listar`
- **Registrar prenda**: `http://localhost:8080/web/prendas/registroPrenda`
- **Gestión de colecciones**: `http://localhost:8080/web/colecciones/listar`

---

## 5. Manual de Usuario

### 5.1 ¿Cómo se ejecuta el proyecto?

**Versión de Java requerida**: Java 17 (JDK 17)

**Comandos de ejecución**:
```powershell
# Modo desarrollo (logs en consola)
mvn spring-boot:run

# Compilar y ejecutar JAR
mvn clean package -DskipTests
java -jar target/*.jar

# Con puerto personalizado
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

**URLs principales**:
- Página raíz: `http://localhost:8080/`
- Prendas: `http://localhost:8080/web/prendas/listar`
- Colecciones: `http://localhost:8080/web/colecciones/listar`

---

### 5.2 Manual Paso a Paso

#### Cómo registrar un elemento padre (Colección)
1. Ir a `http://localhost:8080/web/colecciones/listar`
2. Hacer clic en **"Nueva Colección"**
3. Completar formulario (nombre, temporada, año)
4. Pulsar **Guardar**

#### Cómo registrar un elemento hijo (Prenda)
1. Ir a `http://localhost:8080/web/prendas/listar`
2. Hacer clic en **"Registrar Prenda"**
3. Seleccionar la **Colección** padre
4. Rellenar datos (nombre, talla, color, precio)
5. Pulsar **Guardar**

#### Cómo editar
1. Localizar el elemento en su lista
2. Hacer clic en **Editar**
3. Modificar datos
4. Pulsar **Guardar**

#### Cómo eliminar (eliminación lógica)
1. Localizar el elemento en su lista
2. Hacer clic en **Inactivar** / **Eliminar**
3. Confirmar la acción
4. El elemento se marca como inactivo (no se borra de BD)

#### Cómo ver prendas por colección
- Desde lista de colecciones: clic en **"Ver Prendas"**
- Desde lista de prendas: filtrar por colección
- Acceso directo: `http://localhost:8080/web/prendas/listar?coleccionId=1`

---

## 6. Guía de Instalación y Configuración

### 6.1 Dependencias de Maven

**Esenciales incluidas en pom.xml**:
- `spring-boot-starter-web` - Spring MVC
- `spring-boot-starter-thymeleaf` - Motor de plantillas
- `spring-boot-starter-data-jpa` - Acceso a datos
- `spring-boot-starter-validation` - Validaciones
- `com.mysql:mysql-connector-j` - Conector MySQL
- `org.projectlombok:lombok` - Generación de código

### 6.2 application.properties - Ejemplos de Configuración

#### Para MySQL (Actual)
```properties
spring.application.name=Continua3
server.port=${PORT:8080}

spring.datasource.url=jdbc:mysql://localhost:3306/tienda_ropa
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

#### Para H2 (Desarrollo sin MySQL)
```properties
spring.application.name=Continua3
server.port=8080

spring.datasource.url=jdbc:h2:mem:tienda
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

---

## 7. Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.x, Spring MVC, Spring Data JPA
- **Frontend**: Thymeleaf 3.x, Bootstrap 5, HTML/CSS
- **Base de datos**: MySQL 8.0, Hibernate ORM
- **Validación**: Jakarta Validation API
- **Herramientas**: Maven, Lombok, Docker
- **Control de versiones**: Git

---

## 8. Arquitectura del Proyecto

```
src/main/
  java/com/idat/tienda/Continua3/
    ├── Continua3Application.java
    ├── controller/
    │   ├── PrendasController.java
    │   └── ColeccionController.java
    ├── entity/
    │   ├── PrendasEntity.java
    │   └── ColeccionesEntity.java
    ├── dto/
    │   ├── PrendasDto.java
    │   └── ColeccionesDto.java
    ├── mapper/
    │   ├── PrendasMapper.java
    │   └── ColeccionesMapper.java
    ├── repository/
    │   ├── PrendasRepository.java
    │   └── ColeccionesRepository.java
    ├── servicio/
    │   ├── PrendasServicio.java
    │   └── ColeccionesServicio.java
    └── util/
        └── Constantes.java
  resources/
    ├── application.properties
    ├── static/css/styles.css
    └── templates/
        ├── layout.html
        ├── prendas/
        ├── colecciones/
        └── error/
```

### Patrones Utilizados
- **MVC**: Separación de controladores, vistas y datos
- **DTO + Mapper**: Controllers usan DTOs, servicios usan Entities
- **Eliminación Lógica**: Cambio de estado en lugar de borrado físico
- **Layered Architecture**: Controller → Service → Repository

---

## 9. Rutas Principales

| HTTP | Ruta | Descripción |
|------|------|-------------|
| GET | `/web/prendas/listar` | Listar prendas activas |
| GET | `/web/prendas/registroPrenda` | Formulario nueva prenda |
| POST | `/web/prendas/guardar` | Guardar prenda |
| GET | `/web/prendas/editar/{id}` | Formulario edición |
| POST | `/web/prendas/cambiarEstado/{id}` | Inactivar prenda |
| GET | `/web/colecciones/listar` | Listar colecciones |
| GET | `/web/colecciones/nueva` | Formulario nueva colección |
| POST | `/web/colecciones/guardar` | Guardar colección |
| GET | `/web/colecciones/editar/{id}` | Formulario edición |
| POST | `/web/colecciones/inactivar/{id}` | Inactivar colección |

---

## 10. Capturas del Sistema

### Interfaz Principal
- Página de inicio con navegación
- Listado responsivo de prendas y colecciones
- Formularios validados con Bootstrap

### Funcionalidades Visuales
- ✅ Tablas con datos paginados
- ✅ Botones de acción (Editar, Eliminar, Ver)
- ✅ Formularios con validación cliente/servidor
- ✅ Alertas de éxito y error
- ✅ Páginas de error personalizadas (400, 404, 500)
- ✅ Diseño responsive con Bootstrap 5

### Ejemplo de Uso
1. Acceder a `http://localhost:8080/`
2. Navegar a "Colecciones" → crear nueva colección
3. Navegar a "Prendas" → crear prenda y asignarla a colección
4. Ver listados con filtros y búsqueda
5. Editar o inactivar elementos

---

## 11. Despliegue en Producción

### Render.com
- **Plataforma**: Render.com (PaaS gratuito)
- **Base de datos**: PostgreSQL en Render
- **Docker**: Aplicación containerizada con multi-stage build
- **Dominio**: https://tienda-colecciones-docker.onrender.com

### Docker
```dockerfile
# Ver archivo Dockerfile en raíz del proyecto
FROM maven:3.8-openjdk-17 AS build
# ... construcción del JAR ...

FROM openjdk:17-slim
# ... ejecución con puerto 8080 ...
```

---

## 12. Cambios Recientes (v1.0)

### ✅ Implementado
- Refactorización con DTOs y Mappers
- Validaciones con Jakarta Validation
- Manejo robusto de errores
- Páginas de error personalizadas
- Eliminación lógica de registros
- Código simplificado y "student-friendly"
- Documentación completa

### 📋 Próximas Mejoras
- [ ] ControllerAdvice centralizado
- [ ] Mappers con MapStruct
- [ ] Tests unitarios completos
- [ ] Búsqueda avanzada
- [ ] Reportes en PDF/Excel

---

## 13. Autores

- **Wilder Santamaria Olivos** - Autor Principal
  - GitHub: [@WilderSantamaria18](https://github.com/WilderSantamaria18)
  - Rol: Desarrollo Full Stack

### Entidad Educativa
- **Instituto**: IDAT (Instituto de Data)
- **Materia**: Desarrollo de Servicios Web
- **Ciclo**: Ciclo 3
- **Docente**: [Nombre del docente]

---

## 14. Licencia y Referencias

### Documentación Oficial Consultada
- [Apache Maven Documentation](https://maven.apache.org/)
- [Spring Boot 3.x Reference](https://docs.spring.io/spring-boot/docs/3.5.7/reference/)
- [Spring Data JPA Guide](https://docs.spring.io/spring-data/jpa/reference/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)

### Herramientas Utilizadas
- Spring Boot 3.5.7
- Maven 3.8+
- MySQL 8.0
- Docker
- Git / GitHub

---

## 15. Notas Importantes

### Para Desarrollo Local
- Asegurar que MySQL esté corriendo en puerto 3306
- Verificar credenciales en `application.properties`
- Ejecutar `mvn clean install` si hay problemas de dependencias

### Para Producción (Render)
- Database URL usa PostgreSQL (no MySQL)
- Variables de entorno configuradas en Render dashboard
- Revisar logs en Render si hay errores: https://dashboard.render.com

### Resolución de Problemas Comunes
```bash
# Si falla compilación
mvn clean install -U

# Si puerto 8080 está ocupado
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"

# Si falla conexión BD
# Verificar: host, puerto 3306, usuario root, password 123456, BD tienda_ropa

# Limpiar caché Maven
rm -r ~/.m2/repository
mvn clean install
```

---

**Última actualización**: 2025-11-23
**Versión del proyecto**: 1.0
**Estado**: Producción
