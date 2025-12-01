package com.swen3.paperless.model;

import java.time.Instant;

public record GenAiResultEvent(
        String documentId,
        String summary,
        boolean success,
        String errorMessage,
        String correlationId,
        Instant processedAt
) {}
