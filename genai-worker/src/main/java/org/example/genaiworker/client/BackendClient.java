package org.example.genaiworker.client;

import lombok.extern.slf4j.Slf4j;
import org.example.genaiworker.dto.SummaryRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BackendClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendSummary(Long documentId, String summary) {
        String url = "http://app:8080/api/documents/" + documentId + "/summary";
        log.info("Sending summary for document {} to backend", documentId);

        SummaryRequest request = new SummaryRequest(summary);
        restTemplate.postForObject(url, request, Void.class);

        log.info("Successfully sent summary for document {}", documentId);
    }
}
