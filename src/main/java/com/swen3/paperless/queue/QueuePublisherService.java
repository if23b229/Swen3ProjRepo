package com.swen3.paperless.queue;

import com.swen3.paperless.model.GenAiRequestEvent;
import com.swen3.paperless.model.GenAiResultEvent;
import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;

public interface QueuePublisherService {

    void sendToOcr(OcrRequestEvent event);
    void sendOcrResult(OcrResultEvent event);
    void sendGenAiRequest(GenAiRequestEvent evt);
    void sendGenAiResult(GenAiResultEvent evt);

}