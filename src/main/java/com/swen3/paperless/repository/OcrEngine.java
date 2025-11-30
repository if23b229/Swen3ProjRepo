package com.swen3.paperless.repository;

import java.nio.file.Path;

public interface OcrEngine {
    String extractText(Path pdfPath) throws Exception;
}