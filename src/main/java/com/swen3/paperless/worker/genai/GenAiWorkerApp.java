package com.swen3.paperless.worker.genai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = {
                "com.swen3.paperless.worker.genai",
                "com.swen3.paperless.queue",
                "com.swen3.paperless.service",
                "com.swen3.paperless.config",
                "com.swen3.paperless.model"
        },
        exclude = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
public class GenAiWorkerApp {
    public static void main(String[] args) {
        SpringApplication.run(GenAiWorkerApp.class, args);
    }
}
