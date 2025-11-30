package com.swen3.paperless;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OcrWorkerApp {
    public static void main(String[] args){
        SpringApplication app = new SpringApplication(OcrWorkerApp.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }
}
