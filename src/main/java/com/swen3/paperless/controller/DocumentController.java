package com.swen3.paperless.controller;

import com.swen3.paperless.exception.BadRequestException;
import com.swen3.paperless.exception.TransferFailedException;
import com.swen3.paperless.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadDocument(@RequestPart("file")MultipartFile file) {
        try{
            documentService.uploadDocument(file);
        } catch (BadRequestException | TransferFailedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
