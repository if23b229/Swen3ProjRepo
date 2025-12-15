package org.example.indexingworker;

import org.example.indexingworker.dto.IndexedDocument;
import org.example.indexingworker.dto.OcrResultMessage;
import org.example.indexingworker.listener.IndexingWorkerListener;
import org.example.indexingworker.service.IndexingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class IndexingWorkerListenerTest {

    @Mock
    IndexingService indexingService;

    @InjectMocks
    IndexingWorkerListener listener;

    @Test
    void storesOcrTextInElasticsearch() throws Exception {
        OcrResultMessage msg =
                new OcrResultMessage(42L, "Hello OCR");

        listener.handle(msg);

        verify(indexingService).index(
                new IndexedDocument(42L, "Hello OCR")
        );
    }
}
