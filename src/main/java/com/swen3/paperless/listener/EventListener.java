package com.swen3.paperless.listener;

import com.swen3.paperless.model.Document;
import com.swen3.paperless.model.GenAiResultEvent;
import com.swen3.paperless.model.OcrResultEvent;
import com.swen3.paperless.repository.DocumentRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    private final DocumentRepository documentRepository;

    public EventListener(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }


    @RabbitListener(queues = "${app.mq.resultQueue}")
    public void onOcrResult(OcrResultEvent event) {

        documentRepository.findByDocumentId(event.documentId()).ifPresent(doc -> {
            if (event.success()) {
                doc.setOcrText(event.text());
            }
            documentRepository.save(doc);
        });
    }


    @RabbitListener(queues = "${app.mq.genAiResultQueue}")
    public void onGenAiResult(GenAiResultEvent event) {

        documentRepository.findByDocumentId(event.documentId()).ifPresent(doc -> {
            if (event.success()) {
                doc.setSummary(event.summary());
            }
            documentRepository.save(doc);
        });
    }
}
