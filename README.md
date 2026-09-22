# BFF - Backend for Frontend

## Descripción

Este proyecto es un BFF (Backend for Frontend) desarrollado con Spring Boot y Spring Security. Su función principal es orquestar la comunicación entre el frontend y los distintos microservicios del sistema (`ms-usuarios`, `ms-catálogo` y `ms-pedidos`), validando tokens de Microsoft Entra ID y aplicando reglas de negocio centralizadas, como la gestión de accesos según el rol del usuario.

## Cambios realizados

### 1) Ajuste del flujo de perfil

Se corrigió la lógica de `/api/perfil` para que:

* lea `oid`, `preferred_username` y `name` desde el JWT autenticado.
* delegue la consulta al servicio de usuarios.
* devuelva el objeto `UsuarioResponse` del microservicio, no un `Map` con los claims del token.

Esto permite que el `rol` aparezca en la respuesta, porque el `rol` no viene de Entra ID sino de la base de datos del microservicio de usuarios.

### 2) Eliminación del enfoque de clientes

Se dejó de usar el modelo de clientes y se unificó el flujo hacia el concepto de usuario:

* `UsuarioResponse`
* `UsuarioService`
* `UsuarioRepository`

### 3) Seguridad con JWT de Entra

Se configuró la protección de endpoints con OAuth2 Resource Server:

* Se agruparon las rutas GET (`/api/data`, `/api/perfil`, `/api/productos`, `/api/pedidos`) y la ruta POST (`/api/pedidos`) bajo el permiso `SCOPE_access_as_user`.
* Se habilitó explícitamente la ruta `/error` para facilitar la depuración de excepciones internas.

### 4) Integración de microservicios (Orquestación)

Se configuraron llamadas REST mediante `RestClient` hacia los servicios internos:

* **Usuarios:** `http://localhost:8083` (Búsqueda o creación automática del usuario validando el rol).
* **Catálogo:** `http://localhost:8084` (Obtención de lista de productos mediante `ParameterizedTypeReference`).
* **Pedidos:** `http://localhost:8085` (Creación y listado de pedidos).

### 5) Reglas de Negocio en Pedidos

El BFF actúa como cerebro para el acceso a pedidos:

* Si el usuario tiene rol `ADMIN`, el BFF solicita todos los pedidos almacenados.
* Si el usuario tiene rol `USER` (normal), el BFF envía el `oid` para retornar exclusivamente el historial propio.

### 6) Corrección de entorno de ejecución

Se resolvieron varios problemas de arranque:

* uso de Java 25 con `JAVA_HOME=/usr/lib/jvm/java-25-openjdk`.
* liberación del puerto 8081.
* ajuste del issuer de Entra para que coincida exactamente con el token emitido.

## Endpoints Principales

* **GET /api/perfil:** Requiere autenticación JWT válida. Extrae el OID del token y devuelve el perfil completo con el rol asignado.
* **GET /api/productos:** Devuelve la lista de productos disponibles en el catálogo.
* **GET /api/pedidos:** Devuelve el historial de pedidos aplicando la regla de visibilidad por rol.
* **POST /api/pedidos:** Recibe un carrito de compras y delega la creación del pedido al microservicio correspondiente, calculando internamente los totales.

## Ejemplos de uso por terminal (cURL)

> **⚠️ RECORDATORIO DE TOKEN:** Los tokens JWT de Microsoft Entra ID expiran (usualmente en 1 hora). Si los comandos devuelven un error `401 Unauthorized`, debes generar un nuevo token en Entra ID y actualizar tu variable de entorno ejecutando `export token="TU_NUEVO_TOKEN"` en la terminal antes de probar.

**Crear un nuevo pedido (POST)**

```bash
curl -i -X POST http://localhost:8081/api/pedidos \
  -H "Authorization: Bearer $token" \
  -H "Content-Type: application/json" \
  -d '{
        "items": [
          {
            "nombre": "Mouse gamer Hydra X",
            "precio": 49990.0,
            "cantidad": 2
          },
          {
            "nombre": "Monitor 27\" 144Hz IPS",
            "precio": 249990.0,
            "cantidad": 1
          }
        ]
      }'

```

**Consultar historial de pedidos (GET)**

```bash
curl -i http://localhost:8081/api/pedidos \
  -H "Authorization: Bearer $token"

```

## Estructura principal

* `controller/` → endpoints REST expuestos al frontend.
* `service/` → lógica de negocio y reglas de roles.
* `repository/` → integración y consumo HTTP de los microservicios.
* `dto/` → modelos de solicitud y respuesta.
* `SecurityConfig.java` → configuración de seguridad y CORS.

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
./mvnw clean spring-boot:run

```