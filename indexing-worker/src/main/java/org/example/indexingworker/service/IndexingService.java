package org.example.indexingworker.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.example.indexingworker.dto.IndexedDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class IndexingService {

    private final ElasticsearchClient client;

    public IndexingService(ElasticsearchClient client) {
        this.client = client;
    }

    public void index(IndexedDocument doc) throws IOException {

        for (int i = 1; i <= 10; i++) {
            try {
                client.index(idx -> idx
                        .index("documents")
                        .id(doc.getDocumentId().toString())
                        .document(doc)
                );
                return;
            } catch (Exception e) {
                if (i == 10) throw e;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
