package com.swen3.paperless.worker.genai;

import com.swen3.paperless.model.GenAiRequestEvent;
import com.swen3.paperless.model.GenAiResultEvent;
import com.swen3.paperless.queue.QueuePublisherService;
import com.swen3.paperless.service.GeminiService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GenAiWorker {

    private final QueuePublisherService pub;
    private final GeminiService gemini;

    public GenAiWorker(QueuePublisherService pub, GeminiService gemini) {
        this.pub = pub;
        this.gemini = gemini;
    }

    @RabbitListener(queues = "${app.mq.genAiQueue}")
    public void onGenAiEvent(GenAiRequestEvent event) {
        try {
            String summary = gemini.summarize(event.text());

            pub.sendGenAiResult(new GenAiResultEvent(
                    event.documentId(),
                    summary,
                    true,
                    null,
                    event.correlationId(),
                    Instant.now()
            ));

        } catch (Exception ex) {
            pub.sendGenAiResult(new GenAiResultEvent(
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
