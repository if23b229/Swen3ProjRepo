package com.swen3.paperless.service;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import com.swen3.paperless.repository.QueuePublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QueuePublisherServiceImpl implements QueuePublisherService {
    private static final Logger log = LoggerFactory.getLogger(QueuePublisherServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String ocrKey;
    private final String resultKey;

    public QueuePublisherServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${app.mq.exchange}") String exchange,
            @Value("${app.mq.ocrKey}") String ocrKey,
            @Value("${app.mq.resultKey}") String resultKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.ocrKey = ocrKey;
        this.resultKey = resultKey;
    }
    @Override
    public void sendToOcr(OcrRequestEvent event) {
        log.info("Sending file to OCR");
        rabbitTemplate.convertAndSend(exchange, ocrKey, event);
    }
    @Override
    public void sendOcrResult(OcrResultEvent event) {
        log.info("Sending result from OCR");
        rabbitTemplate.convertAndSend(exchange, resultKey, event);
    }
}
