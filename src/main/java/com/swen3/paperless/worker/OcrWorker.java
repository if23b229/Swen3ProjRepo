package com.swen3.paperless.worker;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OcrWorker {
    @RabbitListener(queues = "${app.mq.resultQueue}")
    public void onOcrResultEvent(OcrResultEvent event) {
        System.out.println("OCR successful: " + event.success());
    }

    @RabbitListener(queues = "${app.mq.ocrQueue}")
    public void onOcrRequestEvent(OcrRequestEvent event) {
        System.out.println("Request OCR for file: " + event.storageUri());
    }
}