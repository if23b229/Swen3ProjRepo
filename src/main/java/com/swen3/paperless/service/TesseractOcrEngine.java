package com.swen3.paperless.service;

import com.swen3.paperless.repository.OcrEngine;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TesseractOcrEngine implements OcrEngine {

    @Override
    public String extractText(Path pdfPath) throws Exception {
        if (!Files.exists(pdfPath)) {
            throw new IllegalArgumentException("PDF file does not exist: " + pdfPath);
        }

        Path tempPng = Files.createTempFile("ocr-page-", ".png");
        convertPdfFirstPageToPng(pdfPath, tempPng);

        String text = runTesseract(tempPng);

        try {
            Files.deleteIfExists(tempPng);
        } catch (IOException ignored) {
        }

        return text;
    }

    private void convertPdfFirstPageToPng(Path pdf, Path outPng) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "gs",
                "-sDEVICE=png16m",
                "-dFirstPage=1",
                "-dLastPage=1",
                "-r300",
                "-o", outPng.toAbsolutePath().toString(),
                pdf.toAbsolutePath().toString()
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int exit = process.waitFor();
        if (exit != 0) {
            String err = readAll(process.getInputStream());
            throw new RuntimeException("Ghostscript failed (exit " + exit + "): " + err);
        }
    }

    private String runTesseract(Path imagePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "tesseract",
                imagePath.toAbsolutePath().toString(),
                "stdout",
                "-l", "eng",
                "--psm", "3"
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        String out = readAll(process.getInputStream());
        int exit = process.waitFor();
        if (exit != 0) {
            throw new RuntimeException("Tesseract failed (exit " + exit + "): " + out);
        }
        return out;
    }

    private String readAll(InputStream in) throws IOException {
        try (in) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int n;
            while ((n = in.read(data)) != -1) {
                buffer.write(data, 0, n);
            }
            return buffer.toString();
        }
    }
}