package com.swen3.paperless.service;

import com.swen3.paperless.repository.OcrEngine;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TesseractOcrEngineTest {

    private final OcrEngine ocrEngine = new TesseractOcrEngine();

    @Test
    void extractText_shouldReturnRecognizedText_whenTesseractIsAvailable() throws Exception {
        // Skip test if tesseract or gs are not available on this machine
        Assumptions.assumeTrue(isCommandAvailable("tesseract"), "tesseract not installed");
        Assumptions.assumeTrue(isCommandAvailable("gs"), "ghostscript (gs) not installed");

        Path pdf = Paths.get("src/test/resources/ocr/sample.pdf");

        String text = ocrEngine.extractText(pdf);

        System.out.println("OCR output:\n" + text);

        assertTrue(
                text.toUpperCase().contains("Hello"),
                "Expected OCR result to contain 'Hello'"
        );
    }

    private boolean isCommandAvailable(String cmd) {
        try {
            Process process = new ProcessBuilder(cmd, "--version")
                    .redirectErrorStream(true)
                    .start();
            int exit = process.waitFor();
            return exit == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}