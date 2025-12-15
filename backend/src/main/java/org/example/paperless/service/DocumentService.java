package org.example.paperless.service;

import org.example.paperless.config.RabbitMQConfig;
import org.example.paperless.dto.DocumentRequest;
import org.example.paperless.dto.DocumentResponse;
import org.example.paperless.exception.NotFoundException;
import org.example.paperless.mapper.DocumentMapper;
import org.example.paperless.model.Document;
import org.example.paperless.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository repository;
    private final DocumentMapper mapper;
    private final RabbitTemplate rabbitTemplate;
    private final MinioStorageService storageService;
    private final SearchService searchService;

    public DocumentService(
            DocumentRepository repository,
            DocumentMapper mapper,
            RabbitTemplate rabbitTemplate,
            MinioStorageService storageService,
            SearchService searchService
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.rabbitTemplate = rabbitTemplate;
        this.storageService = storageService;
        this.searchService = searchService;
    }

    public DocumentResponse create(DocumentRequest request) {
        log.info("Creating document: {}", request);

        Document doc = mapper.toEntity(request);
        Document saved = repository.save(doc);

        log.info("Document saved with ID={}", saved.getId());

        return mapper.toResponse(saved);
    }

    public List<DocumentResponse> getAll() {
        log.debug("Fetching all documents");

        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    public DocumentResponse getById(Long id) {
        log.info("Fetching document with ID={}", id);

        Document doc = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Document {} not found", id);
                    return new RuntimeException("Document not found");
                });

        return mapper.toResponse(doc);
    }

    public DocumentResponse uploadFile(MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());

        try {
            // 1. Save metadata
            Document doc = new Document();
            doc.setFileName(file.getOriginalFilename());
            doc.setMimeType(file.getContentType());
            Document saved = repository.save(doc);

            // 2. Upload to MinIO
            storageService.uploadFile(
                    saved.getId() + ".pdf",
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );

            log.info("Stored file {}.pdf in MinIO", saved.getId());

            // 3. Send RabbitMQ message
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE,
                    saved.getId()
            );

            log.info("Sent message to queue: document ID {}", saved.getId());

            return mapper.toResponse(saved);

        } catch (IOException e) {
            log.error("Error reading uploaded file", e);
            throw new RuntimeException("Failed to read file", e);
        }
    }

    public void saveSummary(Long id, String summary) {
        Document doc = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document not found with id " + id));

        log.info("Saving summary for document {}: {}", id, summary);

        doc.setSummary(summary);
        repository.save(doc);
    }

    public List<DocumentResponse> search(String query) throws IOException {

        log.info("Searching documents with query='{}'", query);

        List<Long> ids = searchService.search(query);

        return repository.findAllById(ids).stream()
                .map(mapper::toResponse)
                .toList();
    }
}
