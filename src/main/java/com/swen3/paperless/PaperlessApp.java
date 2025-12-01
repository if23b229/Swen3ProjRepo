package com.swen3.paperless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.swen3.paperless.config",
                "com.swen3.paperless.controller",
                "com.swen3.paperless.listener",
                "com.swen3.paperless.service",
                "com.swen3.paperless.repository",
                "com.swen3.paperless.model",
                "com.swen3.paperless.queue"
        }
)
public class PaperlessApp {
    public static void main(String[] args) {
        SpringApplication.run(PaperlessApp.class, args);
    }
}
