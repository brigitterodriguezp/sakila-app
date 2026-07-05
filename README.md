# Sakila Rental API

Backend REST para un sistema de alquiler de peliculas usando Spring Boot, PostgreSQL, Sakila, Spring Security, JWT, BCrypt, Spring Data JPA, HikariCP y Swagger/OpenAPI.

Este proyecto no incluye una GUI web funcional. La validacion y uso principal se hacen mediante Swagger UI o clientes HTTP como curl/Postman.

## Tecnologias

## Tecnologías

| Tecnología                     | Uso                                                 |
| ------------------------------ | --------------------------------------------------- |
| Java 17.0.12 LTS               | Runtime del proyecto                                |
| Spring Boot 4.1.0              | Desarrollo de la aplicación REST                    |
| Spring Web 4.1.0               | Creación de controladores y endpoints HTTP          |
| Spring Data JPA 4.1.0          | Persistencia y acceso a PostgreSQL                  |
| Spring Security 4.1.0          | Autenticación y autorización por roles              |
| PostgreSQL 17.10               | Motor de base de datos                              |
| PostgreSQL JDBC Driver 42.7.11 | Conexión entre Spring Boot y PostgreSQL             |
| Sakila for PostgreSQL          | Modelo de datos del sistema de alquiler             |
| HikariCP 7.0.2                 | Pool de conexiones a la base de datos               |
| BCrypt                         | Hash seguro de contraseñas mediante Spring Security |
| JWT / JJWT 0.12.6              | Generación y validación de tokens de autenticación  |
| Springdoc OpenAPI 2.8.5        | Documentación OpenAPI de la API REST                |
| Swagger Core 2.2.28            | Modelado y anotaciones de documentación Swagger     |
| Swagger UI 5.18.3              | Interfaz web para probar los endpoints              |
| Maven 3.9.9                    | Gestión de dependencias, build y ejecución          |
| WAR                            | Empaquetado de la aplicación                        |

## Requisitos

- Java 17.
- PostgreSQL activo.
- Base de datos `sakila`.
- Maven Wrapper incluido en el proyecto.

## Configuracion

Crear `.env` desde la plantilla:

```bash
cp .env.example .env
```

Valores esperados para desarrollo local:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=sakila
DB_USERNAME=brigitte
DB_PASSWORD=b74g1tt3

JWT_SECRET=a2V5c2FraWxhcmVudGFsMjAyNnNlZ3VyaWRhZGp3dGFhMA==
JWT_EXPIRATION=432000000

SERVER_PORT=8080
```

`run.sh` y `run.ps1` cargan `.env` antes de iniciar la aplicacion.

## Base De Datos

Ejecutar setup completo:

```bash
sudo ./db/setup.sh
```

O ejecutar manualmente:

```bash
sudo -u postgres createdb sakila
sudo -u postgres psql -d sakila -f db/credentials.sql
sudo -u postgres psql -d sakila -f db/schema.sql
sudo -u postgres psql -d sakila -f db/seed.sql
sudo -u postgres psql -d sakila -f db/index.sql
```

La aplicacion crea automaticamente usuarios de prueba al iniciar si no existen.

## Ejecutar

Linux/macOS:

```bash
./run.sh
```

Windows PowerShell:

```powershell
.\run.ps1
```

Build WAR:

```bash
./mvnw -DskipTests package
```

## Swagger

Abrir:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## Usuarios De Prueba

| Rol   | Usuario | Password    | Email              | Uso                                                |
| ----- | ------- | ----------- | ------------------ | -------------------------------------------------- |
| ADMIN | `admin` | `Admin123!` | `admin@sakila.app` | Endpoints administrativos y endpoints autenticados |
| USER  | `user`  | `User123!`  | `user@sakila.app`  | Endpoints de consulta, alquiler e historial        |

Notas:

- Las contrasenas se guardan con BCrypt, no en texto plano.
- El usuario `user` queda con id `2`, alineado con `customer_id=2` del seed de Sakila para probar alquileres.
- Los registros creados desde `/api/auth/register` reciben rol `USER`.

## Flujo De Uso De La API

### 1. Verificar Que La App Responde

Swagger debe responder `200`:

```bash
curl -i http://localhost:8080/swagger-ui/index.html
```

### 2. Iniciar Sesion

ADMIN:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin123!"}'
```

USER:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"User123!"}'
```

La respuesta devuelve:

```json
{
  "token": "...",
  "username": "user",
  "role": "USER"
}
```

### 3. Usar El Token

Copiar el token y enviarlo en cada endpoint protegido:

```bash
curl http://localhost:8080/api/categories \
  -H "Authorization: Bearer <TOKEN>"
```

En Swagger:

1. Ejecutar `/api/auth/login`.
2. Copiar `token`.
3. Presionar `Authorize`.
4. Escribir `Bearer <TOKEN>`.
5. Probar endpoints protegidos.

## Endpoints Publicos

| Metodo | Endpoint                 | Autenticacion | Body                                                                    |
| ------ | ------------------------ | ------------- | ----------------------------------------------------------------------- |
| POST   | `/api/auth/login`        | No            | `{"username":"admin","password":"Admin123!"}`                           |
| POST   | `/api/auth/register`     | No            | `{"username":"nuevo","email":"nuevo@sakila.app","password":"User123!"}` |
| GET    | `/swagger-ui/index.html` | No            | Ninguno                                                                 |
| GET    | `/v3/api-docs`           | No            | Ninguno                                                                 |

## Endpoints Para ADMIN O USER

Requieren header:

```text
Authorization: Bearer <TOKEN>
```

| Metodo | Endpoint                          | Rol          | Descripcion                                |
| ------ | --------------------------------- | ------------ | ------------------------------------------ |
| GET    | `/api/categories`                 | ADMIN o USER | Listar categorias                          |
| GET    | `/api/films`                      | ADMIN o USER | Listar peliculas                           |
| GET    | `/api/films/{id}`                 | ADMIN o USER | Consultar pelicula por id                  |
| GET    | `/api/films/search?title=ACADEMY` | ADMIN o USER | Buscar peliculas por titulo                |
| GET    | `/api/rentals/my-active-rentals`  | ADMIN o USER | Alquileres activos del usuario autenticado |
| GET    | `/api/rentals/my-history`         | ADMIN o USER | Historial del usuario autenticado          |
| POST   | `/api/rentals/rent`               | ADMIN o USER | Alquilar pelicula                          |
| PUT    | `/api/rentals/return`             | ADMIN o USER | Devolver alquiler                          |

Ejemplo de alquiler:

```bash
curl -X POST http://localhost:8080/api/rentals/rent \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_USER>" \
  -d '{"filmId":1,"staffId":1}'
```

Ejemplo de devolucion:

```bash
curl -X PUT http://localhost:8080/api/rentals/return \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN_USER>" \
  -d '{"rentalId":3}'
```

## Endpoints Solo ADMIN

Requieren token de `admin`.

| Metodo | Endpoint                        | Body                               | Descripcion                       |
| ------ | ------------------------------- | ---------------------------------- | --------------------------------- |
| POST   | `/api/admin/categories`         | `{"name":"Anime"}`                 | Crear categoria                   |
| PUT    | `/api/admin/categories/{id}`    | `{"id":17,"name":"Anime Updated"}` | Actualizar categoria              |
| DELETE | `/api/admin/categories/{id}`    | Ninguno                            | Eliminar categoria si es posible  |
| POST   | `/api/admin/films`              | Ver ejemplo abajo                  | Crear pelicula                    |
| PUT    | `/api/admin/films/{id}`         | Ver ejemplo abajo                  | Actualizar pelicula               |
| DELETE | `/api/admin/films/{id}`         | Ninguno                            | Eliminar pelicula si es posible   |
| GET    | `/api/admin/inventory?filmId=1` | Ninguno                            | Consultar inventario por pelicula |
| POST   | `/api/admin/inventory`          | `{"filmId":1,"storeId":1}`         | Crear item de inventario          |
| DELETE | `/api/admin/inventory/{id}`     | Ninguno                            | Eliminar item de inventario       |

Ejemplo para crear pelicula:

```json
{
  "title": "NEW TEST FILM",
  "description": "Demo film for API testing",
  "releaseYear": 2026,
  "rating": "PG",
  "rentalRate": 4.99,
  "length": 90,
  "language": "English"
}
```

## Pruebas Rapidas Con Curl

Login y categorias:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"User123!"}' \
  | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

curl http://localhost:8080/api/categories \
  -H "Authorization: Bearer $TOKEN"
```

Login admin y crear categoria:

```bash
ADMIN_TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin123!"}' \
  | sed -n 's/.*"token":"\([^"]*\)".*/\1/p')

curl -X POST http://localhost:8080/api/admin/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -d '{"name":"Anime"}'
```

## Seguridad Y Conexion

- JWT HMAC-SHA256 con secreto Base64 de al menos 256 bits.
- BCrypt strength 10.
- Autorizacion por roles con `ROLE_ADMIN` y `ROLE_USER`.
- HikariCP configurado en `application.properties`.
- `spring.jpa.hibernate.ddl-auto=none`; el esquema se crea con scripts SQL.
- Credenciales reales fuera del repositorio mediante `.env`.
