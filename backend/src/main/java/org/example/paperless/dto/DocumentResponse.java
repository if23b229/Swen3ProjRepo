package org.example.paperless.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DocumentResponse {

    private Long id;
    private String fileName;
    private String mimeType;
    private LocalDateTime uploadDate;
    private String summary;
}
