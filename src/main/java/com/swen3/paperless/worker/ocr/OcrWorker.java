package com.swen3.paperless.worker.ocr;

import com.swen3.paperless.model.GenAiRequestEvent;
import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import com.swen3.paperless.service.MinioStorageService;
import com.swen3.paperless.service.OcrEngine;
import com.swen3.paperless.queue.QueuePublisherService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

@Component
public class OcrWorker {

    private final MinioStorageService minio;
    private final OcrEngine ocr;
    private final QueuePublisherService pub;

    public OcrWorker(MinioStorageService minio, OcrEngine ocr, QueuePublisherService pub) {
        this.minio = minio;
        this.ocr = ocr;
        this.pub = pub;
    }

    @RabbitListener(queues = "${app.mq.ocrQueue}")
    public void onOcrRequestEvent(OcrRequestEvent event) {
        try {

            String uri = event.storageUri().replace("minio://", "");
            String bucket = uri.substring(0, uri.indexOf('/'));
            String object = uri.substring(uri.indexOf('/') + 1);

            Path tempPdf = Files.createTempFile("paperless-ocr-", ".pdf");

            try (InputStream in = minio.download(bucket, object)) {
                Files.copy(in, tempPdf, StandardCopyOption.REPLACE_EXISTING);
            }

            String text = ocr.extractText(tempPdf);

            pub.sendOcrResult(new OcrResultEvent(
                    event.documentId(),
                    text,
                    true,
                    null,
                    event.correlationId(),
                    Instant.now()
            ));

            pub.sendGenAiRequest(new GenAiRequestEvent(
                    event.documentId(),
                    text,
                    event.correlationId(),
                    Instant.now()
            ));

        } catch (Exception ex) {
            pub.sendOcrResult(new OcrResultEvent(
                    event.documentId(),
                    null,
                    false,
                    ex.getMessage(),
                    event.correlationId(),
                    Instant.now()
            ));
        }
    }
}
