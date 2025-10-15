package com.swen3.paperless.model;

import java.time.Instant;

public record OcrRequestEvent(
        String documentId,
        String originalFilename,
        String storageUri,
        Instant uploadedAt,
        String correlationId
) {
}