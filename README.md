# Sistema Hospitalario - Arquitectura de Microservicios

Proyecto académico de gestión básica hospitalaria implementado con Spring Boot y arquitectura de microservicios.

## 1) Qué incluye este proyecto

Este repositorio contiene 4 aplicaciones independientes:

- `pacientes-service` (puerto `8081`)
- `medicos-service` (puerto `8082`)
- `citas-service` (puerto `8083`)
- `api-gateway` (puerto `8080`, entrada única)

## 2) Requisitos previos

Antes de ejecutar, debes tener instalado:

- Java 21 (recomendado)
- Maven Wrapper (ya incluido en cada servicio mediante `mvnw.cmd`)
- PowerShell (Windows)
- Git (opcional, para clonar)
- Docker Desktop (opcional, para levantar todo con Docker Compose)

Comprobar Java:

```powershell
java -version
```

## 3) Clonar el proyecto

```powershell
git clone https://github.com/AckermanChoi/Sistema-Hospitalario.git
cd Sistema-Hospitalario
```

## 4) Estructura del repositorio

```text
Sistema_Hospitalario/
├── pacientes-service/
├── medicos-service/
├── citas-service/
└── api-gateway/
```

Cada carpeta es una aplicación Spring Boot separada, con su propia configuración y arranque.

## 5) Cómo levantar todo (paso a paso, pedagógico)

> Importante: usa **una terminal por servicio** porque cada servidor queda ejecutándose y ocupa su terminal.

### Terminal 1 - Pacientes

```powershell
cd pacientes-service
.\mvnw.cmd spring-boot:run
```

Debes ver algo similar a: `Tomcat started on port 8081`.

### Terminal 2 - Médicos

Desde la raíz del proyecto, abre otra terminal:

```powershell
cd medicos-service
.\mvnw.cmd spring-boot:run
```

Debes ver: `Tomcat started on port 8082`.

### Terminal 3 - Citas

Desde la raíz del proyecto, abre otra terminal:

```powershell
cd citas-service
.\mvnw.cmd spring-boot:run
```

Debes ver: `Tomcat started on port 8083`.

### Terminal 4 - API Gateway

Desde la raíz del proyecto, abre otra terminal:

```powershell
cd api-gateway
.\mvnw.cmd spring-boot:run
```

Debes ver: `Tomcat started on port 8080`.

### Terminal 5 - Pruebas

Usa una terminal adicional para ejecutar llamadas `Invoke-RestMethod`.

---

## 5.1) Levantar todo con Docker Compose (opcional y recomendado)

Si no quieres abrir varias terminales, puedes arrancar todo con un solo comando desde la raíz del proyecto.

### Arrancar servicios

```powershell
docker compose up --build
```

Este comando construye imágenes y levanta:

- `pacientes-service` en `8081`
- `medicos-service` en `8082`
- `citas-service` en `8083`
- `api-gateway` en `8080`

### Ver servicios levantados

```powershell
docker compose ps
```

### Ver logs

```powershell
docker compose logs -f
```

### Parar y limpiar contenedores

```powershell
docker compose down
```

### Pruebas por gateway (cuando está en Docker)

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/pacientes"
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/medicos"
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/citas"
```

---

## 6) Pruebas por microservicio (sin gateway)

## 6.1 Pacientes (`8081`)

### Crear paciente

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8081/api/pacientes" -ContentType "application/json" -Body '{"nombre":"Ana","apellido":"Lopez","dni":"12345678A","fechaNacimiento":"1998-05-20"}'
```

### Listar pacientes

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8081/api/pacientes"
```

### Obtener paciente por ID

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8081/api/pacientes/1"
```

### Actualizar paciente

```powershell
Invoke-RestMethod -Method Put -Uri "http://localhost:8081/api/pacientes/1" -ContentType "application/json" -Body '{"nombre":"Ana Maria","apellido":"Lopez Ruiz","dni":"12345678A","fechaNacimiento":"1998-05-20"}'
```

## 6.2 Médicos (`8082`)

### Crear médico

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8082/api/medicos" -ContentType "application/json" -Body '{"nombre":"Dr. Perez","especialidad":"Cardiologia","numeroColegiado":"COL-1001"}'
```

### Listar médicos

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8082/api/medicos"
```

### Obtener médico por ID

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8082/api/medicos/1"
```

### Buscar por especialidad

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8082/api/medicos?especialidad=Cardiologia"
```

## 6.3 Citas (`8083`)

### Crear cita

> Requiere que ya existan paciente `1` y médico `1`.

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8083/api/citas" -ContentType "application/json" -Body '{"pacienteId":1,"medicoId":1,"fechaHora":"2030-01-10T10:30:00"}'
```

### Listar citas

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8083/api/citas"
```

### Obtener cita por ID

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8083/api/citas/1"
```

### Cancelar cita

```powershell
Invoke-RestMethod -Method Patch -Uri "http://localhost:8083/api/citas/1/cancelar"
```

---

## 7) Pruebas usando una sola entrada (API Gateway en `8080`)

Cuando el gateway esté levantado, usa siempre `8080`:

### Pacientes por gateway

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/pacientes"
```

### Médicos por gateway

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/medicos"
```

### Citas por gateway

```powershell
Invoke-RestMethod -Method Get -Uri "http://localhost:8080/api/citas"
```

Esto demuestra el funcionamiento de **punto único de entrada**.

---

## 8) Documentación interactiva con Swagger

Con los servicios levantados, abre estas URLs en el navegador:

- Pacientes: `http://localhost:8081/swagger-ui/index.html`
- Médicos: `http://localhost:8082/swagger-ui/index.html`
- Citas: `http://localhost:8083/swagger-ui/index.html`

También puedes ver el JSON OpenAPI en:

- Pacientes: `http://localhost:8081/v3/api-docs`
- Médicos: `http://localhost:8082/v3/api-docs`
- Citas: `http://localhost:8083/v3/api-docs`

Uso recomendado para quien evalúa el proyecto:

1. Abrir Swagger UI de cada microservicio.
2. Probar un endpoint `GET` (listado).
3. Probar un endpoint `POST` (creación).
4. Revisar respuestas y códigos HTTP.

---

## 9) Reglas de negocio implementadas

En `citas-service`:

- No permite crear citas con fecha pasada.
- Verifica por REST que el paciente exista.
- Verifica por REST que el médico exista.
- Estado inicial de cita: `PROGRAMADA`.
- Cancelación cambia estado a `CANCELADA`.

## 10) Manejo de errores (resumen)

- `400 Bad Request`: validaciones de entrada o reglas de negocio.
- `404 Not Found`: recurso inexistente (por ejemplo, ID no encontrado).
- `409 Conflict`: duplicados en pacientes/médicos (DNI / número colegiado).
- `500 Internal Server Error`: error no controlado.

## 11) Base de datos

Cada servicio usa H2 en memoria:

- Pacientes: `jdbc:h2:mem:pacientesdb`
- Médicos: `jdbc:h2:mem:medicosdb`
- Citas: `jdbc:h2:mem:citasdb`

La información se pierde al detener cada servicio (comportamiento normal en H2 memoria).

## 12) Cómo detener servicios

En cada terminal de servidor, pulsa:

```text
Ctrl + C
```

## 13) Solución de problemas comunes

### Error: puerto en uso

Significa que otro proceso ya usa ese puerto. Cierra el proceso anterior o cambia `server.port` en `application.properties`.

### Error: `Host desconocido` o URL inválida

Revisa la URL y que incluya `:` entre host y puerto.

Ejemplo correcto:

```text
http://localhost:8081/api/pacientes
```

### Error al crear cita por paciente/médico inexistente

Debes crear primero los recursos en `pacientes-service` y `medicos-service`.

---

## 14) Estado del proyecto

- [x] Microservicio de Pacientes
- [x] Microservicio de Médicos
- [x] Microservicio de Citas
- [x] API Gateway
- [x] Comunicación REST entre servicios
- [x] Persistencia con H2
- [x] DTOs + validaciones + códigos HTTP

---

## 15) Validación rápida (qué debes ver)

Si abres en navegador:

- `http://localhost:8080/api/pacientes`
- `http://localhost:8080/api/medicos`
- `http://localhost:8080/api/citas`

puedes ver `[]` (lista vacía). Eso significa que **la ruta funciona**, pero no hay datos creados todavía.

Después de crear registros con `POST`, al volver a listar ya no saldrá vacío.

### Importante sobre H2 en memoria

Este proyecto usa H2 en memoria, por lo que al reiniciar un servicio sus datos se pierden. Es un comportamiento esperado para este entorno académico.

### Verificar que el gateway está funcionando

El API Gateway enruta peticiones; no expone endpoints de negocio propios como Pacientes/Médicos/Citas.

Puedes validar su estado en:

- `http://localhost:8080/actuator/health`

Y su funcionamiento real probando rutas de negocio por `8080`.

### Error común al arrancar

Si aparece error de Maven por objetivo no encontrado, revisa que el comando sea exactamente:

```powershell
.\mvnw.cmd spring-boot:run
```

No uses `spring-boot:runv` (con `v`), porque ese objetivo no existe.

---

Si quieres mejorar la nota del proyecto, los siguientes extras son buenos candidatos:

- Swagger/OpenAPI
- Dockerización
- Pruebas unitarias adicionales
- Service Discovery (Eureka)
- Circuit Breaker (Resilience4j)
- Seguridad con JWT
