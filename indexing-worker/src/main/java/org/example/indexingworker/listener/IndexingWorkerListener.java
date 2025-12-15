package org.example.indexingworker.listener;

import org.example.indexingworker.dto.IndexedDocument;
import org.example.indexingworker.dto.OcrResultMessage;
import org.example.indexingworker.service.IndexingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class IndexingWorkerListener {

    private final IndexingService indexingService;

    public IndexingWorkerListener(IndexingService indexingService) {
        this.indexingService = indexingService;
    }

    @RabbitListener(queues = "ocr-completed-queue")
    public void handle(OcrResultMessage message) throws Exception {

        indexingService.index(
                new IndexedDocument(
                        message.getDocumentId(),
                        message.getText()
                )
        );
    }
}
