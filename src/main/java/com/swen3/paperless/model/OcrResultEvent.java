package com.swen3.paperless.model;

import java.time.Instant;

public record OcrResultEvent(
        String documentId,
        String text,
        boolean success,
        String errorMessage,
        String correlationId,
        Instant processedAt
) {
}
