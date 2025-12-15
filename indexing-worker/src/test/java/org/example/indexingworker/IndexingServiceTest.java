package org.example.indexingworker;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.example.indexingworker.dto.IndexedDocument;
import org.example.indexingworker.service.IndexingService;
import org.junit.jupiter.api.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
class IndexingServiceTest {

    @Container
    static ElasticsearchContainer es =
            new ElasticsearchContainer(
                    "docker.elastic.co/elasticsearch/elasticsearch:8.19.8"
            ).withEnv("xpack.security.enabled", "false");

    @Test
    void indexesDocument() throws Exception {

        ElasticsearchClient client =
                new ElasticsearchClient(
                        new RestClientTransport(
                                RestClient.builder(
                                        HttpHost.create(es.getHttpHostAddress())
                                ).build(),
                                new JacksonJsonpMapper()
                        )
                );

        IndexingService service = new IndexingService(client);

        service.index(new IndexedDocument(1L, "Hello World"));

        var get = client.get(g -> g
                        .index("documents")
                        .id("1"),
                IndexedDocument.class
        );

        assertTrue(get.found());
    }
}
