package com.swen3.paperless.model;

import java.time.Instant;

public record GenAiRequestEvent(
        String documentId,
        String text,
        String correlationId,
        Instant createdAt
) {}
