package org.example.paperless.mapper;

import org.example.paperless.dto.DocumentRequest;
import org.example.paperless.dto.DocumentResponse;
import org.example.paperless.model.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    Document toEntity(DocumentRequest request);

    DocumentResponse toResponse(Document document);
}
