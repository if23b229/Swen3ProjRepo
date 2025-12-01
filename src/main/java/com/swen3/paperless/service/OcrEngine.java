package com.swen3.paperless.service;

import java.nio.file.Path;

public interface OcrEngine {
    String extractText(Path pdfPath) throws Exception;
}