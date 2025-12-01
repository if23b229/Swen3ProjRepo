package com.swen3.paperless.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String documentId;

    private String filename;

    private String storageUri;

    @Column(columnDefinition = "TEXT")
    private String ocrText;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private Instant uploadedAt;

    public Document() {}

    public Document(String documentId, String filename, String storageUri, Instant uploadedAt) {
        this.documentId = documentId;
        this.filename = filename;
        this.storageUri = storageUri;
        this.uploadedAt = uploadedAt;
    }

    public UUID getId() { return id; }
    public String getDocumentId() { return documentId; }
    public String getFilename() { return filename; }
    public String getStorageUri() { return storageUri; }
    public String getOcrText() { return ocrText; }
    public String getSummary() { return summary; }
    public Instant getUploadedAt() { return uploadedAt; }

    public void setOcrText(String ocrText) { this.ocrText = ocrText; }
    public void setSummary(String summary) { this.summary = summary; }
}
