# BFF - Backend for Frontend

## Descripción
Este proyecto es un BFF (Backend for Frontend) desarrollado con Spring Boot y Spring Security. Su función principal es validar tokens de Microsoft Entra ID, autenticar al usuario y consultar el microservicio de usuarios para obtener el perfil completo de cada usuario, incluido el rol que se gestiona en el microservicio.

## Cambios realizados hoy

### 1) Ajuste del flujo de perfil
Se corrigió la lógica de `/api/perfil` para que:
- lea `oid`, `preferred_username` y `name` desde el JWT autenticado
- delegue la consulta al servicio de usuarios
- devuelva el objeto `UsuarioResponse` del microservicio, no un `Map` con los claims del token

Esto permite que el `rol` aparezca en la respuesta, porque el `rol` no viene de Entra ID sino de la base de datos del microservicio de usuarios.

### 2) Eliminación del enfoque de clientes
Se dejó de usar el modelo de clientes y se unificó el flujo hacia el concepto de usuario:
- `UsuarioResponse`
- `UsuarioService`
- `UsuarioRepository`

### 3) Seguridad con JWT de Entra
Se configuró la protección de endpoints con OAuth2 Resource Server:
- `/api/data` protegido con `SCOPE_access_as_user`
- `/api/perfil` protegido con `SCOPE_access_as_user`

### 4) Integración con microservicio de usuarios
Se configuran llamadas REST hacia el microservicio de usuarios en:
- `http://localhost:8083`

El flujo implementado es:
- buscar usuario por `oid`
- si no existe, crear el usuario con datos del JWT
- devolver el perfil completo del microservicio

### 5) Corrección de entorno de ejecución
Se resolvieron varios problemas de arranque:
- uso de Java 25 con `JAVA_HOME=/usr/lib/jvm/java-25-openjdk`
- liberación del puerto 8081
- ajuste del issuer de Entra para que coincida exactamente con el token emitido

### 6) Validación del proyecto
Se ejecutó la validación con Maven y el proyecto quedó en estado de build exitoso:
- `./mvnw test -q`
- `./mvnw spring-boot:run`

## Endpoint principal

### GET /api/perfil
Este endpoint requiere autenticación JWT válida.

Ejemplo de intención:
- extraer `oid` del token
- consultar el microservicio de usuarios
- devolver el perfil completo del usuario con rol

## Estructura principal
- `controller/` → endpoints REST
- `service/` → lógica de negocio
- `repository/` → integración con el microservicio
- `dto/` → modelos de respuesta
- `SecurityConfig.java` → configuración de seguridad

## Variables de entorno recomendadas
```bash
export ENTRA_ISSUER_URI="https://login.microsoftonline.com/<tenant-id>/v2.0"
export ENTRA_API_CLIENT_ID="<application-client-id>"
```

## Ejecución
```bash
cd /home/sh1r8/Escritorio/duoc/Cloud/api
export JAVA_HOME=/usr/lib/jvm/java-25-openjdk
export PATH="$JAVA_HOME/bin:$PATH"
./mvnw spring-boot:run
```

## Estado
Proyecto funcionando y validado con build exitoso.
