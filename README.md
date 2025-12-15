# paperless

## How to start

mvn clean package

docker compose up --build

## if first start: 
indexing-worker, 
lifecycle, 
package 

Paperless ist ein verteiltes, containerisiertes Dokumentenmanagement-System.
Es ermöglicht das Hochladen von Dokumenten, automatische Texterkennung (OCR), KI-basierte Zusammenfassungen sowie eine performante Volltextsuche über Elasticsearch.

Das Projekt ist microservice-basiert, nutzt asynchrone Verarbeitung mit RabbitMQ und ist vollständig über Docker Compose lauffähig.

Features 
Dokumenten-Upload (PDF), Metadatenverwaltung (PostgreSQL), Dateiablage in MinIO (S3-kompatibel), OCR-Verarbeitung (asynchron)
, KI-basierte Zusammenfassungen (GenAI Worker), Volltextsuche mit Elasticsearch, Event-basierte Verarbeitung mit RabbitMQ, Komplett containerisiert mit Docker


## Services
### Backend (backend)

REST API für Dokumente, Speichert Metadaten in PostgreSQL, Lädt Dateien in MinIO hoch, Stellt Such-Endpoint bereit, Empfängt KI-Zusammenfassungen

### Wichtige Endpoints

POST   /api/documents/upload

GET    /api/documents

GET    /api/documents/{id}

GET    /api/documents/search?q=...

POST   /api/documents/{id}/summary
 
### Worker (ocr-worker)

Lauscht auf Upload-Events, Lädt Dokumente aus MinIO, Extrahiert Text (OCR), Sendet OCR-Ergebnis an RabbitMQ

### Indexing Worker (indexing-worker)

Lauscht auf ocr-completed-queue, Indexiert Dokumente in Elasticsearch, Verwendet documentId als Elasticsearch _id

### GenAI Worker (genai-worker)

Lauscht auf OCR-Ergebnisse,
Erstellt KI-basierte Zusammenfassungen,
Sendet Summary an das Backend

### Benötigt einen API-Key (z. B. Google Gemini)

Elasticsearch,
Volltextsuche über OCR-Inhalte,
Index: documents

### Felder:

documentId,
content

### PostgreSQL

Persistiert Dokument-Metadaten,
Tabellen automatisch via JPA erzeugt

### MinIO

S3-kompatibler Objektspeicher,
Bucket: documents

### RabbitMQ

Asynchrone Kommunikation
Queues:
document-upload-queue
ocr-completed-queue

### Lokales Setup Voraussetzungen

Docker
Docker Compose
Java 21 & Maven



### Nach dem Start sind verfügbar:

Service	URL
Backend API	http://localhost:8080

RabbitMQ UI	http://localhost:15672

MinIO Console	http://localhost:9001

Elasticsearch	http://localhost:9200

UI	http://localhost

Environment Variables

