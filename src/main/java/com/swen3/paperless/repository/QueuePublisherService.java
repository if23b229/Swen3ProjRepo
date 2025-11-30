package com.swen3.paperless.repository;

import com.swen3.paperless.model.OcrRequestEvent;
import com.swen3.paperless.model.OcrResultEvent;

public interface QueuePublisherService {

    void sendToOcr(OcrRequestEvent event);

    void sendOcrResult(OcrResultEvent event);
}