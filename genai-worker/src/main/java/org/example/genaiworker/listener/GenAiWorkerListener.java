package org.example.genaiworker.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.genaiworker.config.RabbitMQConfig;
import org.example.genaiworker.dto.OcrResultMessage;
import org.example.genaiworker.service.GenAiService;
import org.example.genaiworker.client.BackendClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenAiWorkerListener {

    private final GenAiService genAiService;
    private final BackendClient backendClient;

    public GenAiWorkerListener(GenAiService genAiService, BackendClient backendClient) {
        this.genAiService = genAiService;
        this.backendClient = backendClient;
    }

    @RabbitListener(queues = RabbitMQConfig.OCR_COMPLETED_QUEUE)
    public void onOcrCompleted(OcrResultMessage msg) {
        log.info("GenAI Worker received OCR text for document {}", msg.getDocumentId());

        String summary = genAiService.generateSummary(msg.getText());
        log.info("Generated summary for document {}: {}", msg.getDocumentId(), summary);

        backendClient.sendSummary(msg.getDocumentId(), summary);
    }
}
