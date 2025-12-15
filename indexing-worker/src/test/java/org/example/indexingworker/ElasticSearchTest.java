package org.example.indexingworker;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

class ElasticSearchTest {

    private static final String BASE_URL =
            "http://localhost:8080/api/documents/search";

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    void searchHelloShouldReturnHttp200_whenDockerIsRunning() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        BASE_URL + "?q=Hello",
                        String.class
                );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void searchWorldShouldReturnHttp200_whenDockerIsRunning() {

        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        BASE_URL + "?q=World",
                        String.class
                );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
    }
}
