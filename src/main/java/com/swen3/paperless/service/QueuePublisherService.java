package com.swen3.paperless.service;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueuePublisherService {
    private static final Logger log = LoggerFactory.getLogger(QueuePublisherService.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String ocrKey;
    private final String resultKey;

    public QueuePublisherService(
            RabbitTemplate rabbitTemplate,
            @Value("${app.mq.exchange}") String exchange,
            @Value("${app.mq.ocrKey}") String ocrKey,
            @Value("${app.mq.resultKey}") String resultKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.ocrKey = ocrKey;
        this.resultKey = resultKey;
    }

    public void sendToOcr(OcrRequestEvent event) {
        log.info("Sending file to OCR");
        rabbitTemplate.convertAndSend(exchange, ocrKey, event);
    }

    public void sendOcrResult(OcrResultEvent event) {
        log.info("Sending result from OCR");
        rabbitTemplate.convertAndSend(exchange, resultKey, event);
    }
}
