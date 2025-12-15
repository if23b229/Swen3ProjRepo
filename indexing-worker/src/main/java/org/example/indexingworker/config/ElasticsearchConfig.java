package org.example.indexingworker.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {

        RestClient restClient = RestClient.builder(
                HttpHost.create("http://elasticsearch:9200")
        ).build();

        ElasticsearchClient client = new ElasticsearchClient(
                new RestClientTransport(
                        restClient,
                        new JacksonJsonpMapper()
                )
        );

        waitForElasticsearch(client);
        return client;
    }

    private void waitForElasticsearch(ElasticsearchClient client) {
        for (int i = 1; i <= 30; i++) {
            try {
                if (client.ping().value()) {
                    System.out.println("Elasticsearch is reachable");
                    return;
                }
            } catch (Exception ignored) {}

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {}
        }
        throw new IllegalStateException("Elasticsearch not reachable after startup");
    }
}
