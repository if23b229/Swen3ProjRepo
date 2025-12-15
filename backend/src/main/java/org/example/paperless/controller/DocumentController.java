package org.example.paperless.controller;

import jakarta.validation.Valid;
import org.example.paperless.dto.DocumentRequest;
import org.example.paperless.dto.DocumentResponse;
import org.example.paperless.dto.SummaryRequest;
import org.example.paperless.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService service;

    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<DocumentResponse> getAll() {
        return service.getAll();
    }

    @PostMapping
    public DocumentResponse create(@Valid @RequestBody DocumentRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/upload")
    public DocumentResponse upload(
            @RequestParam("file") MultipartFile file
    ) {
        return service.uploadFile(file);
    }

    @PostMapping("/{id}/summary")
    public ResponseEntity<Void> saveSummary(
            @PathVariable Long id,
            @RequestBody SummaryRequest req) {

        service.saveSummary(id, req.getSummary());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public List<DocumentResponse> search(@RequestParam String q) throws IOException {
        return service.search(q);
    }
}
