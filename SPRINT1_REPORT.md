# Sprint 1 – Project Setup, REST API, DAL

## Überblick
Im Rahmen des SWEN3-Projekts wurde in Sprint 1 das Grundgerüst für das Paperless DMS erstellt.  
Ziel war es, ein lauffähiges Projekt mit Spring Boot, einer ersten REST API sowie einer Data Access Layer (DAL) Struktur aufzubauen.

GitHub Repo.Url: https://github.com/if23b229/Swen3ProjRepo.git

---

## Projekt Setup
- Repository erstellt (Main-Branch → stabil und buildbar).
- Maven als Build-Tool eingerichtet (`pom.xml` mit Spring Boot 3, JDK 21).
- IntelliJ IDEA als IDE verwendet.
- Hauptklasse: `PaperlessApp` im Package `com.swen3.paperless`.

---

## REST API
### Endpunkte
- **Health Endpoint**
  - `GET /api/v1/health`  
  - Antwort: `{"status": "UP"}`

- **Message Endpoints**
  - `POST /api/v1/messages`  
    - Request (JSON):  
      ```json
      { "text": "Hello DB", "author": "Nertil" }
      ```
    - Antwort (JSON mit generierter UUID):  
      ```json
      { "id": "uuid", "text": "Hello DB", "author": "Nertil" }
      ```

  - `GET /api/v1/messages`  
    - Antwort: Liste aller gespeicherten Messages.

---

## Data Access Layer (DAL)
- **Entity:** `Message` (`id` als UUID, `text`, `author`)
- **Repository:** `MessageRepository` (Interface, extends `JpaRepository`)
- **Datenbank:** H2 In-Memory (Hibernate erstellt Tabelle automatisch)

Beispiel-Tabelle `MESSAGES`:
| id (UUID) | text      | author |
|-----------|-----------|--------|
| 123e…     | Hello DB  | Nertil |

---

## Konfiguration
`src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:paperlessdb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

server.port=8080
logging.level.root=INFO
