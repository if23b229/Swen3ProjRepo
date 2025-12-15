package org.example.ocrworker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DOCUMENT_UPLOAD_QUEUE = "document-upload-queue";

    @Bean
    public Queue documentUploadQueue() {
        return new Queue(DOCUMENT_UPLOAD_QUEUE, true);
    }

    public static final String OCR_COMPLETED_QUEUE = "ocr-completed-queue";

    @Bean
    public Queue ocrCompletedQueue() {
        return new Queue(OCR_COMPLETED_QUEUE, true);
    }
}
