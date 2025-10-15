package com.swen3.paperless.worker;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OcrWorker {
    @RabbitListener(queues = "${app.mq.resultQueue}")
    public void onOcrResultEvent(OcrResultEvent result) {
        System.out.println("Hello Ocr Worker Result");
    }

    @RabbitListener(queues = "${app.mq.ocrQueue}")
    public void onOcrRequestEvent(OcrRequestEvent request) {
        System.out.println("Hello Ocr Worker");
    }
}