package com.swen3.paperless.worker;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;
import com.swen3.paperless.service.MinioStorageService;
import com.swen3.paperless.service.OcrEngine;
import com.swen3.paperless.queue.QueuePublisherService;
import com.swen3.paperless.worker.ocr.OcrWorker;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OcrWorkerTest {

    @Test
    public void onOcrRequestEvent_shouldDownloadRunOcrAndPublish() throws Exception{

        MinioStorageService minioStorageService = mock(MinioStorageService.class);
        OcrEngine ocrEngine = mock(OcrEngine.class);
        QueuePublisherService queuePublisherService = mock(QueuePublisherService.class);

        OcrWorker ocrWorker = new OcrWorker(minioStorageService,ocrEngine,queuePublisherService);

        OcrRequestEvent ocrRequestEvent = new OcrRequestEvent(
                "doc123",
                "file.pdf",
                "minio://documents/doc123.pdf",
                Instant.now(),
                "corr-xyz"
        );

        when(minioStorageService.download("documents", "doc123.pdf"))
                .thenReturn(new ByteArrayInputStream(new byte[0]));
        when(ocrEngine.extractText(any())).thenReturn("Hello OCR");

        ocrWorker.onOcrRequestEvent(ocrRequestEvent);

        verify(minioStorageService).download("documents", "doc123.pdf");
        verify(ocrEngine).extractText(any());

        ArgumentCaptor<OcrResultEvent> captor = ArgumentCaptor.forClass(OcrResultEvent.class);
        verify(queuePublisherService).sendOcrResult(captor.capture());

        OcrResultEvent result = captor.getValue();

        assertTrue(result.success());
        assertTrue(result.text().contains("Hello OCR"));

    }

}