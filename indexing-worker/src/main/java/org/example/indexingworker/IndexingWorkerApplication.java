package org.example.indexingworker;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class IndexingWorkerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexingWorkerApplication.class, args);
    }

}
