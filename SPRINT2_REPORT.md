# SPRINT 2 — Report (Paperless / SWEN3_DMS)

> **Status:** Completed  
> **Stack:** Spring Boot (API) · Nginx (UI) · PostgreSQL · Adminer · Docker Compose

---

## Sprint Goal
Deliver a **runnable Dockerized core**:
- Static **Web UI** served by **nginx**
- **REST API** (Spring Boot) reachable via nginx reverse-proxy
- **PostgreSQL** as DB + **Adminer** for inspection
- One E2E feature: **Messages** (GET/POST) persisted in Postgres

---

## 2) Delivered

### 2.1 Containers & Connectivity
- **paperless-ui** (nginx) on `:8085` serves static assets and **proxies `/api/*` → `api:8080`**  
- **paperless-api** (Spring Boot) on `:8080` with endpoint **`/api/v1/messages`** (GET/POST)  
- **paperless-postgres** on `:5432` (DB `paperless`)  
- **adminer** on `:9091` (DB console)

**Flow:** Browser → `http://localhost:8085` (UI) → nginx proxy → API → Postgres (no CORS).

### 2.2 Feature (MVP)
- **Messages**
  - `GET /api/v1/messages` — list
  - `POST /api/v1/messages` — create `{ text, author }`
- Verified persistence via **Adminer**.

### 2.3 Config & Build
`src/main/resources/application.properties` (ENV-driven):
```properties
server.port=${SERVER_PORT:8080}
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/paperless}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:paperless}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:paperless}
# dev only; use 'validate' in prod
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:update}
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
