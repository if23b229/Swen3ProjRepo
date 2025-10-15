package com.swen3.paperless.dto;



public record DocumentUploadDto(
        String documentId,
        String storageUri,
        String originalFilename,
        String contentType,
        String correlationId
) {}