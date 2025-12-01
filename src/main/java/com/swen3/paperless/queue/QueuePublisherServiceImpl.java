package com.swen3.paperless.queue;

import com.swen3.paperless.model.*;
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
    private final String genAiKey;
    private final String genAiResultKey;

    public QueuePublisherServiceImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${app.mq.exchange}") String exchange,
            @Value("${app.mq.ocrKey}") String ocrKey,
            @Value("${app.mq.resultKey}") String resultKey,
            @Value("${app.mq.genAiKey}") String genAiKey,
            @Value("${app.mq.genAiResultKey}") String genAiResultKey
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.ocrKey = ocrKey;
        this.resultKey = resultKey;
        this.genAiKey = genAiKey;
        this.genAiResultKey = genAiResultKey;
    }

    @Override
    public void sendToOcr(OcrRequestEvent event) {
        log.info("Publishing OCR request");
        rabbitTemplate.convertAndSend(exchange, ocrKey, event);
    }

    @Override
    public void sendOcrResult(OcrResultEvent event) {
        log.info("Publishing OCR result");
        rabbitTemplate.convertAndSend(exchange, resultKey, event);
    }

    @Override
    public void sendGenAiRequest(GenAiRequestEvent evt) {
        log.info("Publishing GenAI request");
        rabbitTemplate.convertAndSend(exchange, genAiKey, evt);
    }

    @Override
    public void sendGenAiResult(GenAiResultEvent evt) {
        log.info("Publishing GenAI result");
        rabbitTemplate.convertAndSend(exchange, genAiResultKey, evt);
    }
}
