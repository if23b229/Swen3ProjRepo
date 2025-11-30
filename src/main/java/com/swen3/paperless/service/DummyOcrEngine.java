package com.swen3.paperless.service;

import com.swen3.paperless.repository.OcrEngine;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class DummyOcrEngine implements OcrEngine {

    @Override
    public String extractText(Path pdfPath) {
        // TODO: replace with real Tesseract logic later
        return "Dummy OCR result for file: " + pdfPath.getFileName();
    }
}