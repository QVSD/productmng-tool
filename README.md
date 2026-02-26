# Store Management API

A production-oriented Spring Boot REST API designed as a store management tool.


---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security (HTTP Basic)
- PostgreSQL (Docker)
- Maven
- JUnit 5 + Mockito

---

## Architecture Overview

The project follows a layered architecture:

controller -> service -> repository -> database

### Structure

```

controller
service
repository
model (entity + dto)
mapper
security
exception
config

````

### Design Principles

- Separation of concerns (DTO vs Entity)
- Business logic isolated in service layer
- Centralized exception handling
- Production-like security configuration
- Optimistic locking for concurrency safety

---

## Security Design

The API is secured using:

- HTTP Basic Authentication
- Stateless session management
- BCrypt password encoding
- Role-based endpoint authorization
- Custom 401/403 JSON responses

### Roles

| Role  | Permissions |
|--------|-------------|
| USER   | Read-only access |
| ADMIN  | Create, update price, delete |

### Security Considerations

- No stack traces exposed in API responses
- No sensitive data logged
- Authentication failures logged (without credentials)
- Access violations logged with user and IP address

---

## Concurrency Handling

The `Product` entity uses optimistic locking via:

```java
@Version
private Long version;
````

If two concurrent updates occur, the API returns:

```
HTTP 409 Conflict
```

This prevents lost updates and ensures data integrity.

---

## API Endpoints

### Create Product (ADMIN only)

POST /api/products

### Get Product (USER / ADMIN)

GET /api/products/{id}

### List Products (USER / ADMIN)

GET /api/products

### Change Price (ADMIN only)

PATCH /api/products/{id}/price

### Delete Product (ADMIN only)

DELETE /api/products/{id}

---

## Error Handling

All exceptions are handled through a centralized `GlobalExceptionHandler`.

Standardized error response format:

```json
{
  "timestamp": "2026-01-01T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/products/1"
}
```

Handled cases:

* 400 – Validation errors
* 401 – Authentication required
* 403 – Access denied
* 404 – Resource not found
* 409 – Conflict (duplicate / concurrent modification)
* 500 – Unexpected error

---

## Logging

Logging is implemented for:

* Product creation
* Price changes
* Product deletion
* Authentication failures
* Access denial attempts

A correlation ID is attached to each request to improve traceability.

Sensitive data such as passwords or credentials are never logged.

---

## Testing

The application includes both unit and web-layer tests.

### Service Layer (Unit Tests)

- Business logic validation
- Happy path scenarios
- Exception scenarios (not found, duplicate)
- Concurrency behavior
- Price update behavior

Mockito is used to isolate dependencies.

### Web Layer Tests

Using `@WebMvcTest`:

- 200 OK responses for valid requests
- 400 for validation failures
- 404 for missing resources
- 401 for unauthenticated requests
- 403 for insufficient roles

Security configuration and global exception handling
are validated through dedicated web-layer tests.

---

## Running the Application

### 1. Start PostgreSQL

```
docker-compose up -d
```

### 2. Run the application

```
mvn spring-boot:run
```

Server runs on:

```
http://localhost:8080
```

---

## Future Enhancements

If extended for a real-world production system:

* Replace HTTP Basic with JWT or OAuth2
* Externalized user persistence
* Rate limiting for brute-force protection
* Structured JSON logging (ELK integration)
* Audit trail persistence
* Containerization with Kubernetes
* CI/CD pipeline

---

## Assumptions

* Minimal role model (USER / ADMIN)
* PostgreSQL used for realistic persistence
* API designed as standalone service

---

## Author

Dragos