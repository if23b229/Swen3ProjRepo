package com.swen3.paperless.worker;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import com.swen3.paperless.repository.MinioStorageService;
import com.swen3.paperless.repository.OcrEngine;
import com.swen3.paperless.repository.QueuePublisherService;
import com.swen3.paperless.service.QueuePublisherServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Component
public class OcrWorker {

    private final MinioStorageService minioStorageService;
    private final OcrEngine ocrEngine;
    private final QueuePublisherService queuePublisherService;

    public OcrWorker(MinioStorageService minioStorageService, OcrEngine ocrEngine, QueuePublisherService queuePublisherService) {
        this.minioStorageService = minioStorageService;
        this.ocrEngine = ocrEngine;
        this.queuePublisherService = queuePublisherService;
    }


    @RabbitListener(queues = "${app.mq.ocrQueue}")
    public void onOcrRequestEvent(OcrRequestEvent event) {
        try {
            String uri = event.storageUri();
            String withoutPrefix = uri.replace("minio://", "");
            String bucket = withoutPrefix.substring(0, withoutPrefix.indexOf('/'));
            String objectName = withoutPrefix.substring(withoutPrefix.indexOf('/') + 1);

            Path tempPdf = Files.createTempFile("paperless-ocr-", ".pdf");
            try (InputStream input = minioStorageService.download(bucket, objectName)) {
                Files.copy(input, tempPdf, StandardCopyOption.REPLACE_EXISTING);
            }
            String text = ocrEngine.extractText(tempPdf);
            queuePublisherService.sendOcrResult(new OcrResultEvent(
                    event.documentId(),
                    text,
                    true,
                    null,
                    event.correlationId(),
                    Instant.now()
            ));
        } catch (Exception ex) {
            queuePublisherService.sendOcrResult(new OcrResultEvent(
                    event.documentId(),
                    null,
                    false,
                    ex.getMessage(),
                    event.correlationId(),
                    Instant.now()
            ));
        }

    }


    @RabbitListener(queues = "${app.mq.resultQueue}")
    public void onOcrResultEvent(OcrResultEvent event) {
        System.out.println("OCR successful: " + event.success());
    }
}