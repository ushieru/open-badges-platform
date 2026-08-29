# SPEC — Visibilidad de Emisiones por Organización

> **Estado:** Propuesta
> **Módulos involucrados:** `issuer`, `badgeclass`, `assertion`, `membership`, `account`
> **Rama sugerida:** `feat/org-issuance-reporting`

---

## 1. Contexto y Problema

Actualmente una organización (Issuer) emite insignias (Assertions de un BadgeClass) a
recipientes por email, pero **no existe forma de que sus administradores consulten el
historial de lo emitido**:

- qué credenciales (badges) ha emitido la organización,
- qué personas recibieron cada badge,
- y si cada insignia ya fue **reclamada** por su recipiente.

El único acceso actual es la consulta de *mis* credenciales (`GET /api/admin/me/assertions`),
que solo devuelve las del usuario autenticado, y el endpoint público
`GET /api/v2/assertions/{uuid}` que expone una única acreditación pública.

Este SPEC propone la funcionalidad para que **OWNER y ADMIN** de una organización puedan
auditar las emisiones realizadas, respetando la **privacidad por diseño** (los emails de
recipientes se almacenan como hash SHA-256).

---

## 2. Objetivos

1. Permitir a los administradores de una organización listar los **badges emitidos** por ella,
   con su contador de emisiones y de reclamaciones.
2. Permitir consultar, por badge, las **personas recipientes** con el **estado de reclamación**
   (reclamada / no reclamada / revocada).
3. Respetar la **privacidad**: nunca exponer emails en texto plano de recipientes no reclamados.
4. Aplicar **seguridad por rol**: solo `OWNER` y `ADMIN` de la organización acceden a sus datos.

---

## 3. Roles y Permisos

| Rol      | Ver resumen de emisiones | Ver detalle por badge | Emitir nuevas | Gestionar miembros |
| -------- | ------------------------ | --------------------- | ------------- | ------------------ |
| `OWNER`  | ✅                       | ✅                    | ✅            | ✅                 |
| `ADMIN`  | ✅                       | ✅                    | ❌ (solo OWNER) | ✅                 |

La autenticación se valida con la anotación existente `@OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })`
(a través de `OrgSecurityFilter`), reutilizando la infraestructura de seguridad actual.

---

## 4. Modelo de Dominio (referencia)

```
Issuer (organización)
 └── BadgeClass (credencial)
      └── Assertion (emisión individual)
           ├── recipientEmail   → hash SHA-256 del email (nunca texto plano)
           ├── account          → null si NO reclamada; Account si reclamada
           ├── isPublic         → true cuando está reclamada/pública
           ├── isRevoked        → estado de revocación
           ├── evidence         → URL de evidencia opcional
           └── issuedOn         → fecha de emisión
```

### Determinación del estado de reclamación

| Estado       | Criterio                                                    |
| ------------ | ----------------------------------------------------------- |
| **Reclamada**  | `assertion.account != null` (equivalente a `isPublic == true`) |
| **No reclamada** | `assertion.account == null`                                  |
| **Revocada**    | `assertion.isRevoked == true`                                |

> Nota: cuando se emite a un email cuyo `Account` ya existe, la assertion nace reclamada.
> Si el `Account` no existe, nace sin reclamar y se reclamará al vincular el email
> (ver `ClaimAssertions`).

---

## 5. Contratos de API

### 5.1 Resumen de badges emitidos

```
GET /api/v2/issuers/{issuerUuid}/badges/analytics
```

Devuelve, para cada badge de la organización, métricas agregadas de emisión.

**Auth:** `@Authenticated` + `@OrgRole({ OWNER, ADMIN })`

**Response `200 OK`**

```json
{
  "data": [
    {
      "badgeId": "f1f21e11-c76d-4cda-a2b3-103b2ca39903",
      "name": "Core Contributor",
      "imageUrl": "https://.../image.png",
      "issued": 42,
      "claimed": 31,
      "pending": 9,
      "revoked": 2,
      "claimRate": 73.81
    }
  ],
  "meta": {
    "totalRecords": 5,
    "currentPage": 1,
    "totalPages": 1,
    "nextPage": null,
    "prevPage": null
  }
}
```

**Query params:** hereda `PaginationRequestParams` (`page`, `size`).

### 5.2 Detalle de recipientes por badge

```
GET /api/v2/issuers/{issuerUuid}/badges/{badgeClassUuid}/assertions
```

Devuelve las emisiones de una credencial concreta, indicando el estado de cada una,
con **filtros avanzados** opcionales.

**Auth:** `@Authenticated` + `@OrgRole({ OWNER, ADMIN })`

**Query params (filtros):**

| Param     | Tipo     | Descripción                                        |
| --------- | -------- | -------------------------------------------------- |
| `page`    | `int`    | Página actual (1-based). Hereda `PaginationRequestParams` |
| `size`    | `int`    | Tamaño de página (max 100). Hereda `PaginationRequestParams` |
| `sort`    | `string` | Orden, p.ej. `issuedOn,desc` o `name,asc`. **Requerido agregar a `PaginationRequestParams`** (el frontend ya lo envía vía `useParams`) |
| `status`  | `string` | Filtra por estado: `CLAIMED`, `PENDING`, `REVOKED`  |
| `search`  | `string` | Búsqueda parcial sobre `recipientEmail` (hash) o `account.fullName` cuando está reclamada |
| `from`    | `date`   | Emisiones desde esta fecha (`issuedOn >= from`)     |
| `to`      | `date`   | Emisiones hasta esta fecha (`issuedOn <= to`)       |

**Response `200 OK`**

```json
{
  "data": [
    {
      "assertionId": "edb7dd30-690a-4901-beb6-21af30a80ba0",
      "recipient": {
        "fullName": "Ana Torres",
        "email": "ana.torres@example.com"
      },
      "status": "CLAIMED",
      "issuedOn": "2026-08-01T10:15:30Z",
      "evidence": "https://...",
      "isPublic": true
    },
    {
      "assertionId": "91b2cc44-...",
      "recipient": {
        "fullName": null,
        "email": null
      },
      "status": "PENDING",
      "issuedOn": "2026-08-03T09:00:00Z",
      "evidence": null,
      "isPublic": false
    }
  ],
  "meta": {
    "totalRecords": 42,
    "currentPage": 1,
    "totalPages": 5,
    "nextPage": 2,
    "prevPage": null
  }
}
```

**Reglas de exposición del recipient:**

| Estado         | `fullName`             | `email` (texto plano)         |
| -------------- | ---------------------- | ----------------------------- |
| `CLAIMED`      | `account.fullName`     | `account.email` (sí)          |
| `PENDING`      | `null`                 | `null` (solo hash interno)    |
| `REVOKED`      | `account.fullName` (si fue reclamada antes de revocarse) | igual que CLAIMED |

> Para recipientes **no reclamados** nunca se devuelve el email. Opcionalmente se puede
> incluir `recipientHash` (el hash SHA-256) para que el administrador pueda correlacionar
> sin romper privacidad.

### 5.2.1 Búsqueda por criterios avanzados (`PanacheCriteria`)

El listado usa el utilitario **`PanacheCriteria`** de
`src/main/java/com/gdgguadalajara/common/utils/PanacheCriteria.java` (evitando HQL crudo).

```java
public PaginatedResponse<BadgeAssertionItem> run(
        UUID badgeClassUuid, AssertionFilterParams filters, PaginationRequestParams params) {

    var criteria = PanacheCriteria.<Assertion>of(Assertion.class)
            .eq("badgeClass.id", badgeClassUuid)
            .like("recipientEmail", filters.search());           // hash parcial
            // .eq("isRevoked", ...)  según status

    if (filters.status() != null)
        switch (filters.status()) {
            case CLAIMED -> criteria.isNotNull("account").eq("isRevoked", false);
            case PENDING -> criteria.isNull("account");
            case REVOKED -> criteria.eq("isRevoked", true);
        }
    if (filters.from() != null) criteria.ge("issuedOn", filters.from());
    if (filters.to() != null)   criteria.le("issuedOn", filters.to());

    var page = criteria.orderBy(params.sortOrDefault("issuedOn,desc"))
            .page(params.page, params.size)
            .getResult();
    // mapear cada Assertion → BadgeAssertionItem
}
```

> **Nota:** `PanacheCriteria` expone `eq`, `ne`, `like`, `between`, `in`, `isNull`,
> `isNotNull`, `gt/ge/lt/le`, `orderBy`, `page` y `firstResult`, suficientes para estos
> filtros sin escribir consultas HQL a mano (según convención de `AGENTS.md`).
>
> **Nota sobre `sort`:** `PaginationRequestParams` actualmente solo tiene `page` y `size`.
> El frontend ya envía `sort` (ver `useParams`), por lo que este SPEC asume que se agrega
> `@QueryParam("sort") String sort` a `PaginationRequestParams` como parte de la
> implementación, sin romper los consumidores existentes.

### 5.3 DTOs propuestos

```java
// model/dto/BadgeIssuanceSummary.java
public record BadgeIssuanceSummary(
        UUID badgeId,
        String name,
        String imageUrl,
        long issued,
        long claimed,
        long pending,
        long revoked,
        double claimRate) {
}

// model/dto/BadgeAssertionRecipient.java
public record BadgeAssertionRecipient(
        String fullName,
        String email) {
}

// model/dto/BadgeAssertionItem.java
public record BadgeAssertionItem(
        UUID assertionId,
        BadgeAssertionRecipient recipient,
        String status,          // CLAIMED | PENDING | REVOKED
        Instant issuedOn,
        String evidence,
        boolean isPublic) {
}
```

**Enum `AssertionStatus`:** `CLAIMED`, `PENDING`, `REVOKED` (modelo del módulo `assertion`).

---

## 6. Diseño de Implementación

Siguiendo las reglas de arquitectura de `AGENTS.md` (capas `Resource → application → model`):

### Módulos nuevos

```
src/main/java/com/gdgguadalajara/issuer/analytics/
├── IssuerAnalyticsResource.java        # REST: /api/v2/issuers/{issuerUuid}/badges/analytics
└── application/
    ├── BuildBadgeIssuanceSummary.java  # run(issuerUuid) → List<BadgeIssuanceSummary>
    └── ListBadgeAssertions.java        # run(badgeClassUuid, page, size) → PaginatedResponse<BadgeAssertionItem>
```

### Casos de uso (application)

**`BuildBadgeIssuanceSummary.run(UUID issuerUuid)`**
- `@Transactional`
- Consulta agregada con `Assertion` por `badgeClass.issuer.id = ?1` agrupando por `badgeClass`.
- Para cada badge computa: `issued` (total), `claimed` (account != null), `revoked`
  (isRevoked), `pending` (issued - claimed), `claimRate` = `claimed / issued * 100`
  redondeado a 2 decimales.
- Si `issued == 0` por badge, se omiten (o se incluyen con `claimRate = 0`; se decide incluir
  solo badges con al menos una emisión para no ensuciar el resumen).

**`ListBadgeAssertions.run(UUID badgeClassUuid, AssertionFilterParams filters, PaginationRequestParams params)`**
- `@Transactional`
- Construye la consulta con `PanacheCriteria.of(Assertion.class)` encadenando
  `.eq("badgeClass.id", badgeClassUuid)`, filtros por `status` (con `isNull`/`isNotNull`/`eq`),
  rango de fechas (`ge`/`le`) y búsqueda (`like` sobre `recipientEmail`).
- Aplica `.orderBy(params.sort)` y `.page(params.page, params.size).getResult()`.
- Mapea cada `Assertion` a `BadgeAssertionItem` aplicando las reglas de exposición del §5.2.

### DTO de filtros avanzados

```java
// model/dto/AssertionFilterParams.java
public record AssertionFilterParams(
        @QueryParam("status") AssertionStatus status,
        @QueryParam("search") String search,
        @QueryParam("from") LocalDate from,
        @QueryParam("to") LocalDate to) {
}
```

### Resource (REST)

```java
@Path("/api/v2/issuers/{issuerUuid}/badges")
public class IssuerAnalyticsResource {

    private final BuildBadgeIssuanceSummary buildBadgeIssuanceSummary;
    private final ListBadgeAssertions listBadgeAssertions;

    @GET
    @Path("/analytics")
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public PaginatedResponse<BadgeIssuanceSummary> summary(UUID issuerUuid,
            @BeanParam @Valid PaginationRequestParams params) { ... }

    @GET
    @Path("/{badgeClassUuid}/assertions")
    @Authenticated
    @OrgRole({ MemberRole.OWNER, MemberRole.ADMIN })
    public PaginatedResponse<BadgeAssertionItem> assertions(UUID issuerUuid, UUID badgeClassUuid,
            @BeanParam @Valid AssertionFilterParams filters,
            @BeanParam @Valid PaginationRequestParams params) {
        return listBadgeAssertions.run(badgeClassUuid, filters, params);
    }
}
```

> ⚠️ **Conflicto de ruta:** el resource `IssuerBadgeClassResource` ya expone
> `GET /api/v2/issuers/{issuerUuid}/badges`. Las nuevas rutas deben convivir sin colisión:
> `analytics` y `/{badgeClassUuid}/assertions` no chocan con el `GET` base. Se recomienda
> ubicar el nuevo resource en un subpath nuevo (`.../badges/analytics` y
> `.../badges/{id}/assertions`) y validar el registro en el arranque.

---

## 7. UI / Frontend (Nuxt)

### 7.1 Pantalla: resumen de emisiones de la organización

- Ruta: `GET /organizations/{id}` → nueva sección **"Emisiones"** visible solo para
  `OWNER`/`ADMIN` (reutilizando los wrappers `OnlySuperUsersOrMembers`/`OnlyMembers`).
- Tabla de badges emitidos con columnas: **Credencial**, **Emitidas**, **Reclamadas**,
  **Pendientes**, **Revocadas**, **% Reclamación** (barra de progreso).
- Al hacer clic en una fila → navega al detalle de esa credencial.

### 7.2 Pantalla: detalle de recipientes por credencial

- Ruta: `GET /organizations/{id}/badges/{badgeId}/emissions`
- Lista de recipientes con:
  - **Nombre** (si reclamada, `account.fullName`),
  - **Email** (si reclamada),
  - **Estado** como badge visual:
    - `CLAIMED` → badge teal con palomita,
    - `PENDING` → badge dorado "Pendiente",
    - `REVOKED` → badge rojo "Revocada",
  - **Fecha de emisión** (`issuedOn`).
- Acción por fila: enlace a la assertion pública (`/api/v2/assertions/{id}`).

### 7.3 Servicios Orval

Agregar los endpoints al OpenAPI y regenerar con `npm run build` (orval). Crear
`services/issuer-analytics-resource/...` con los métodos `get...Url` / `get...`.

---

## 8. Consideraciones de Seguridad y Privacidad

1. **Acceso por rol:** todos los endpoints del módulo se protegen con
   `@OrgRole({ OWNER, ADMIN })`. `OrgSecurityFilter` valida que el usuario autenticado tenga
   membresía activa en el `issuerUuid`.
2. **No exponer hashes de forma innecesaria:** el email en texto plano solo se revela para
   assertions `CLAIMED` (donde el `Account` ya existe y el recipiente consintió su cuenta).
3. **Recipientes pendientes:** se oculta `email` y `fullName`; se ofrece el hash solo si se
   necesita correlación manual.
4. **Auditoría:** no se requieren eventos de auditoría adicionales en esta iteración; las
   queries son de solo lectura.

---

## 9. Criterios de Aceptación

- [ ] Un `OWNER` y un `ADMIN` pueden ver el resumen de badges emitidos de su organización.
- [ ] Un `ADMIN` de otra organización recibe `403` (validado por `OrgSecurityFilter`).
- [ ] El detalle por badge muestra el estado `CLAIMED`/`PENDING`/`REVOKED` correctamente.
- [ ] Los filtros `status`, `search`, `from` y `to` funcionan en el listado de recipientes.
- [ ] Un recipiente no reclamado no expone su email ni nombre en la respuesta.
- [ ] El resumen incluye `claimRate` calculado correctamente (0–100, 2 decimales).
- [ ] La paginación y el `sort` funcionan con los `PaginationRequestParams` estándar.
- [ ] La UI solo muestra las secciones de emisiones a roles con permiso.
- [ ] `npm run build` (orval) regenera los servicios sin errores.

---

## 10. Fuera de Alcance (futuras iteraciones)

- Revocar desde la vista de emisiones (ver **SPEC — Revocación de Badges Entregadas** en
  `SPECS/revoke-badges.md`).
- Exportación CSV/PDF del reporte.
- Notificaciones al recipiente pendiente para reclamar.