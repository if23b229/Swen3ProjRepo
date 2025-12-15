package org.example.genaiworker.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String OCR_COMPLETED_QUEUE = "ocr-completed-queue";

    @Bean
    public Queue ocrCompletedQueue() {
        return new Queue(OCR_COMPLETED_QUEUE, true);
    }
}
