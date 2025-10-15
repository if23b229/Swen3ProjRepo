package com.swen3.paperless.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;


@RestController
public class HealthController {

    @GetMapping("/api/v1/health")//Das ist eine Annotation von Spring Boot, die eine HTTP-Route definiert.
    public Map<String, String> health() {
        return Map.of("status", "App running");
    }
}
