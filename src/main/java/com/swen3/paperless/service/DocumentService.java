package com.swen3.paperless.service;

import com.swen3.paperless.exception.BadRequestException;
import com.swen3.paperless.exception.TransferFailedException;
import com.swen3.paperless.model.OcrRequestEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentService {
    private final QueuePublisherService queuePublisherService;
    private final Path uploadRoot;

    public DocumentService(QueuePublisherService queuePublisherService) {
        this.queuePublisherService = queuePublisherService;
        this.uploadRoot = Paths.get("uploads").toAbsolutePath().normalize();
    }

    public void uploadDocument(MultipartFile file) throws BadRequestException, TransferFailedException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BadRequestException("Invalid Filename");
        }
        if (!originalFilename.endsWith(".txt")) {
            throw new BadRequestException("Only .txt files are allowed");
        }

        try {
            Files.createDirectories(uploadRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String documentId = UUID.randomUUID().toString();
        String safeName = documentId + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = uploadRoot.resolve(safeName);

        try  {
            InputStream in = file.getInputStream();
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String correlationId = UUID.randomUUID().toString();

        var event = new OcrRequestEvent(
                documentId,
                file.getOriginalFilename(),
                target.toString(),
                Instant.now(),
                correlationId
        );

        queuePublisherService.sendToOcr(event);
    }
}
