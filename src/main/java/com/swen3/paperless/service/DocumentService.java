package com.swen3.paperless.service;

import com.swen3.paperless.exception.BadRequestException;
import com.swen3.paperless.exception.TransferFailedException;
import com.swen3.paperless.model.Document;
import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.queue.QueuePublisherServiceImpl;
import com.swen3.paperless.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Profile("api")
@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final QueuePublisherServiceImpl queuePublisherService;
    private final MinioStorageService minioStorageService;
    private final DocumentRepository documentRepository;

    public DocumentService(
            QueuePublisherServiceImpl queuePublisherService,
            MinioStorageService minioStorageService,
            DocumentRepository documentRepository   // <-- HERE!!!
    ) {
        this.queuePublisherService = queuePublisherService;
        this.minioStorageService = minioStorageService;
        this.documentRepository = documentRepository;
    }

    public void uploadDocument(MultipartFile file) throws BadRequestException, TransferFailedException {

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Invalid Filename");
        }
        if (!originalFilename.endsWith(".pdf")) {
            throw new BadRequestException("Only .pdf files are allowed");
        }

        String storageUri;
        try {
            storageUri = minioStorageService.uploadPdf(file);
        } catch (RuntimeException e) {
            log.error("Failed to upload file to MinIO", e);
            throw new TransferFailedException();
        }

        String documentId = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();

        // Save metadata in DB
        Document doc = new Document(
                documentId,
                originalFilename,
                storageUri,
                Instant.now()
        );

        documentRepository.save(doc);   // <-- THIS NOW WORKS ✔

        // Send OCR request
        OcrRequestEvent event = new OcrRequestEvent(
                documentId,
                originalFilename,
                storageUri,
                Instant.now(),
                correlationId
        );

        queuePublisherService.sendToOcr(event);
    }
}
