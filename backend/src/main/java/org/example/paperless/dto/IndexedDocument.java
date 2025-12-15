package org.example.paperless.dto;

import lombok.Data;

@Data
public class IndexedDocument {
    private Long documentId;
    private String content;
}
