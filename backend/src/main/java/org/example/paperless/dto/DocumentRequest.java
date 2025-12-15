package org.example.paperless.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentRequest {

    @NotBlank
    private String fileName;

    @NotBlank
    private String mimeType;
}
