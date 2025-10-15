package com.swen3.paperless.service;

import com.swen3.paperless.exception.BadRequestException;
import com.swen3.paperless.exception.TransferFailedException;
import com.swen3.paperless.model.OcrRequestEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentService {
    private final QueuePublisherService queuePublisherService;

    public DocumentService(QueuePublisherService queuePublisherService) {
        this.queuePublisherService = queuePublisherService;
    }

    public void uploadDocument(MultipartFile file) throws BadRequestException, TransferFailedException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Invalid Filename");
        }
        if (!originalFilename.endsWith(".txt")) {
            throw new BadRequestException("Only .txt files are allowed");
        }

        File savedFile = new File("uploads/" + file.getOriginalFilename());
        savedFile.getParentFile().mkdirs();

        try {
            file.transferTo(savedFile);
        } catch (IOException e) {
            throw new TransferFailedException("File could not be transferred");
        }

        String documentId = UUID.randomUUID().toString();
        String storageUri = savedFile.getAbsolutePath();
        String correlationId = UUID.randomUUID().toString();

        var event = new OcrRequestEvent(
                documentId,
                file.getOriginalFilename(),
                storageUri,
                Instant.now(),
                correlationId
        );

        queuePublisherService.sendToOcr(event);
    }
}
