# 🗂️ SWEN3 Paperless DMS

## 📖 Overview
This project is part of the **SWEN3** course — a **Document Management System (DMS)** designed to demonstrate modern backend development practices.

**Sprint 1** focuses on delivering the foundational setup, which includes:
- ✅ **Spring Boot 3** application structure
- 🌐 **REST API** endpoints
- 💾 **Data Access Layer (DAL)** using an **in-memory H2 database**

---

## 🚀 Getting Started

### 🧩 Requirements
Before running the project, ensure you have the following installed:

- **Java** 21 (or higher LTS)
- **Maven** 3.9+
- **Docker** and **Docker Compose**

---

## 🐳 Build & Run with Docker

To build and start all containers locally:

```bash
docker compose down   # Stop any running containers
docker compose build  # Build all images
docker compose up -d  # Run containers in detached mode
```

This will start the application along with all required services (e.g., database, message broker, etc. in later sprints).

---

## 💻 Run Locally (Without Docker)

If you prefer to run the application directly on your system:

```bash
mvn clean package
mvn spring-boot:run
```

The REST API will be available at:

```
http://localhost:8080/api/v1
```

## 🧱 Technologies Used
- **Spring Boot 3**
- **Maven**
- **H2 Database**
- **Docker / Docker Compose**
- **Java 21**
