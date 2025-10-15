package com.swen3.paperless.controller;


import com.swen3.paperless.dto.DocumentUploadDto;
import com.swen3.paperless.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> enqueue(@RequestBody DocumentUploadDto dto) {
        documentService.uploadDocument(dto);
        return ResponseEntity.ok().build();
    }
}
