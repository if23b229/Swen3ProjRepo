package com.swen3.paperless.service;

import com.swen3.paperless.exception.BadRequestException;
import com.swen3.paperless.exception.TransferFailedException;
import com.swen3.paperless.model.OcrRequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentService {
    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final QueuePublisherService queuePublisherService;
    private final MinioStorageService minioStorageService;

    public DocumentService(QueuePublisherService queuePublisherService, MinioStorageService minioStorageService) {
        this.queuePublisherService = queuePublisherService;
        this.minioStorageService = minioStorageService;
    }

    public void uploadDocument(MultipartFile file) throws BadRequestException, TransferFailedException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Invalid Filename");
        }
        if (!originalFilename.endsWith(".txt")) {
            throw new BadRequestException("Only .txt files are allowed");
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
