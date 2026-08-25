# AGENTS.md

Open Badges Platform: Quarkus (Java 21) backend serving a Nuxt (Vue 3) frontend via Quarkus Quinoa, PostgreSQL + Flyway. Frontend: Tailwind CSS v4.

## 1. Tech Stack
- **Framework:** Quarkus (Supersonic Subatomic Java)
- **Language:** Java 21
- **Build Tool:** Gradle
- **Persistence:** Hibernate ORM with Panache (Active Record pattern)
- **Database:** SQLite (Local file storage at `./data/seccion_amarilla.db`)
- **Utilities:** Lombok, Jackson (JSON), Hibernate Validator, SmallRye OpenAPI

---

## 2. Module Structure (Architecture)
The project is strictly modularized by feature. Every new module MUST follow this exact folder structure:

```text
src/main/java/com/gdgguadalajara/[module_name]/
├── [ModuleName]Resource.java          # Resource Layer (REST endpoints)
├── application/                       # Business Logic Layer
│   └── [ActionName].java              # Use cases (e.g., CreateAccount, ActivateAccount)
└── model/                             # Domain Layer
    ├── [EntityName].java              # Panache Entity
    ├── [EnumName].java                # Enums (if any)
    └── dto/                           # Data Transfer Objects
        └── [Action]Request.java       # Java Records for request payloads

```

---

## 3. Strict Layer Rules & Patterns

### A. Model Layer (Domain & DTOs)

* **Entities:** Must extend `PanacheEntityBase`.
* **Primary Keys:** Use `UUID` generated via `@GeneratedValue(strategy = GenerationType.UUID)`.
* **Timestamps:** Use `@CreationTimestamp` (updatable = false) and `@UpdateTimestamp` for audit fields.
* **DTOs:** MUST be implemented as Java `record`.
* **Validation:** Always use `jakarta.validation.constraints` (e.g., `@NotBlank`) inside DTO records. These records will be the input parameters for REST endpoints.

### B. Business Logic Layer (Application)

* **Single Responsibility:** Each class in the `application` package must represent a single use case (e.g., `CreateAccount`, `UpdateApplication`).
* **The `run()` Method:** All business logic MUST be executed through a single method named `run()`.
* **Method Overloading:** You may overload the `run()` method to handle different signatures or actors (e.g., `run(id, status, user)` vs `run(id, status, company)`).
* **Injection:** Classes MUST be annotated with `@ApplicationScoped`. Dependencies (including other application use cases or EventBus) MUST be injected via constructor using Lombok (`@AllArgsConstructor` or `@RequiredArgsConstructor`). Always declare injected dependencies as `private final`.
* **Transactions:** Use `@Transactional` on the class or `run()` method only when database mutations occur.
* **Error Handling:** NEVER return null or generic exceptions for business rules. Always throw `DomainException` (e.g., `DomainException.notFound(...)`, `DomainException.forbidden(...)`, `DomainException.badRequest(...)`).

### C. Resource Layer (REST API)

* **Verbs:** Use standard JAX-RS annotations (`@GET`, `@POST`, `@PUT`, `@DELETE`).
* **Thin Controllers:** The Resource class MUST NOT contain business logic. It delegates mutations to the injected `application` classes.
* **Injection:** Inject application classes using Lombok's constructor injection (`private final`).
* **Simple Reads:** Simple queries (like listing all) can be done directly in the Resource layer (e.g., `Account.listAll()`).
* **Nested Resources:** For sub-resources involving multiple modules, create a dedicated resource file (e.g., `AccountServiceResource.java`) and nest the path: `@Path("/api/v1/accounts/{uuid}/services")`.

---

## 4. Specific System Patterns

### A. Domain Exceptions

When applying business rules in the `application` layer, use the predefined `DomainException` mapper:

```java
// Examples of valid exception throwing:
if (entity == null) throw DomainException.notFound("Recurso no encontrado");
if (invalid) throw DomainException.badRequest("Estado invalido");
if (unauthorized) throw DomainException.forbidden("[Error no explicito de permisos, usar cosas como 'Recurso no disponible' o 'Entidad no encontrada']");

```

### B. Pagination and Complex Queries (PanacheCriteria)

For GET endpoints that require filtering, sorting, or pagination, NEVER write raw HQL strings. You MUST use the `common` module's `PanacheCriteria` and `PaginationRequestParams` pattern.

**Endpoint implementation example:**

```java
@GET
public PaginatedResponse<Category> readAvailables(@BeanParam @Valid PaginationRequestParams params) {
    return PanacheCriteria.of(Category.class)
            .eq("isEnabled", true)
            .le("availableFrom", LocalDate.now())
            // ... more conditions ...
            .page(params.page, params.size)
            .orderBy(params.sort)
            .getResult();
}

```

## 5. Development Conventions

* **Live Coding:** Run `./gradlew quarkusDev -Dvertx.disableURIValidation=true`.
* **Language:** Variable names, classes, and methods MUST be in English. Error messages and validation strings returned to the user MUST be in Spanish.
* **Type Inference:** Always prefer using var for local variable declarations to keep the code concise and readable.