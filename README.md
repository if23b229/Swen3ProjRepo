# SWEN3 Paperless DMS

## Overview
This project is part of the SWEN3 course — a Document Management System (DMS) that demonstrates modern backend development practices such as REST APIs, object storage, message queues, asynchronous workers, and OCR processing.

This submission includes all required features:

- Spring Boot 3 REST API
- Uploading PDF documents into MinIO object storage
- Asynchronous OCR pipeline using RabbitMQ
- Dedicated OCR Worker (separate Spring Boot application)
- Tesseract OCR and Ghostscript integration
- Unit tests for OCR engine and worker logic
- Fully containerized setup using Docker Compose
- Adminer for database inspection
- Nginx for static UI hosting

---

## System Architecture

REST API (PaperlessApp)
- Stores files in MinIO
- Publishes OcrRequestEvent to RabbitMQ

RabbitMQ
- Routes OCR tasks to the worker

OCR Worker (OcrWorkerApp)
- Downloads the PDF from MinIO
- Runs OCR using Tesseract and Ghostscript
- Publishes OcrResultEvent back to RabbitMQ

MinIO
- Stores original PDF files

PostgreSQL
- Stores metadata (future sprints)

Nginx UI
- Serves static frontend files

Adminer
- Database inspector for PostgreSQL

---

## Requirements

Install the following before running the project:

- Java 21+
- Maven 3.9+
- Docker and Docker Compose
- (Optional) Tesseract OCR and Ghostscript for running OCR tests locally

---

## Build and Run with Docker

To build and start the entire system:
-docker compose down
-docker compose build
-docker compose up -d
### Services Started by Docker

- REST API: http://localhost:8080/api/v1
- UI (Nginx): http://localhost:8085
- MinIO Console: http://localhost:9001
- RabbitMQ Management UI: http://localhost:15672
- Adminer: http://localhost:9091

RabbitMQ default credentials:
- user: guest
- pass: guest

MinIO default credentials:
- user: minio
- pass: minio12345

---

## Run Locally Without Docker
mvn clean package AND
mvn spring-boot:run

---

## Uploading a Document

Endpoint:

POST /api/v1/document

Form-data:
- file: yourfile.pdf

Process:
1. API stores the PDF in MinIO
2. API publishes an OcrRequestEvent to RabbitMQ
3. OCR worker downloads the file
4. Tesseract extracts the text
5. Worker publishes OcrResultEvent
6. API receives the event

---

## Testing

### Unit Tests Implemented

- TesseractOcrEngineTest
    - Tests OCR on a sample PDF
    - Skips the test if Tesseract or Ghostscript are not installed locally

- OcrWorkerTest
    - Uses mock MinIO and mock OCR engine
    - Verifies:
        - MinIO download is called
        - OCR engine is called
        - QueuePublisherService sends result event

Open MinioUI for Testing and visual feedback on stored pdf visit:
http://localhost:9001


Login Credentials 

Username: minio 

Password: minio12345
