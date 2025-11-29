package com.swen3.paperless.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Bean
    public MinioClient minioClient(
            @Value("${app.minio.endpoint}") String endpoint,
            @Value("${app.minio.accessKey}") String accessKey,
            @Value("${app.minio.secretKey}") String secretKey

    ){
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
