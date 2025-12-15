package org.example.paperless.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.example.paperless.dto.IndexedDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchService {

    private final ElasticsearchClient client;

    public SearchService(ElasticsearchClient client) {
        this.client = client;
    }

    public List<Long> search(String query) throws IOException {

        var response = client.search(s -> s
                        .index("documents")
                        .query(q -> q
                                .match(m -> m
                                        .field("content")
                                        .query(query)
                                )
                        ),
                IndexedDocument.class
        );

        return response.hits().hits().stream()
                .map(hit -> Long.valueOf(hit.id()))
                .toList();
    }
}
