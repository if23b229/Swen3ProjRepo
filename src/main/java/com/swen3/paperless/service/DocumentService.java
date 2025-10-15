package com.swen3.paperless.service;

import com.swen3.paperless.dto.DocumentUploadDto;
import com.swen3.paperless.model.OcrRequestEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentService {
    private final QueuePublisherService queuePublisherService;

    public DocumentService(QueuePublisherService queuePublisherService) {
        this.queuePublisherService = queuePublisherService;
    }

    public void uploadDocument(DocumentUploadDto dto){
        String corrId = getCorrelationId(dto);

        var evt = new OcrRequestEvent(
                dto.documentId(),
                dto.originalFilename(),
                dto.storageUri(),
                Instant.now(),
                corrId
        );

        queuePublisherService.sendToOcr(evt);
    }

    private String getCorrelationId(DocumentUploadDto dto) {
        String correlationId = dto.correlationId();
        if (correlationId != null && !correlationId.isBlank()) {
            return correlationId;
        } else {
            return UUID.randomUUID().toString();
        }
    }
}
