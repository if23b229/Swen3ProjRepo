package org.example.ocrworker.listener;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.example.ocrworker.config.RabbitMQConfig;
import org.example.ocrworker.dto.OcrResultMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;

@Service
@Slf4j
public class OcrWorkerListener {

    private final MinioClient minioClient;
    private final RabbitTemplate rabbitTemplate;

    public OcrWorkerListener(MinioClient minioClient,
                             RabbitTemplate rabbitTemplate) {
        this.minioClient = minioClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.DOCUMENT_UPLOAD_QUEUE)
    public void onDocumentUpload(Long documentId) {
        log.info("OCR Worker received message for document ID={}", documentId);

        String objectName = documentId + ".pdf";

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket("documents").object(objectName).build())) {

            log.info("Downloaded PDF {} from MinIO", objectName);

            // OCR durchführen (dein Code)
            String text = performOCR(stream);
            log.info("OCR Result: {}", text);

            // 🔥 OCR-Resultat an GenAI Worker schicken
            OcrResultMessage msg = new OcrResultMessage(documentId, text);
            rabbitTemplate.convertAndSend(RabbitMQConfig.OCR_COMPLETED_QUEUE, msg);

            log.info("Sent OCR result for document ID={} to queue '{}'",
                    documentId, RabbitMQConfig.OCR_COMPLETED_QUEUE);

        } catch (Exception e) {
            log.error("Error processing document {}", documentId, e);
        }
    }

    // Dein OCR Code unverändert 👇
    private String performOCR(InputStream pdfStream) throws Exception {

        File tempPdf = File.createTempFile("paperless-", ".pdf");
        try (FileOutputStream out = new FileOutputStream(tempPdf)) {
            pdfStream.transferTo(out);
        }

        File tempPng = File.createTempFile("paperless-", ".png");

        Process gs = new ProcessBuilder(
                "gs",
                "-dNOPAUSE",
                "-dBATCH",
                "-sDEVICE=png16m",
                "-r300",
                "-sOutputFile=" + tempPng.getAbsolutePath(),
                tempPdf.getAbsolutePath()
        ).redirectErrorStream(true).start();

        int gsExit = gs.waitFor();
        if (gsExit != 0) {
            String err = new String(gs.getInputStream().readAllBytes());
            throw new RuntimeException("Ghostscript failed: " + err);
        }

        File tempTxtBase = File.createTempFile("paperless-ocr-", "");
        String outputBase = tempTxtBase.getAbsolutePath();

        Process tesseract = new ProcessBuilder(
                "tesseract",
                tempPng.getAbsolutePath(),
                outputBase
        ).redirectErrorStream(true).start();

        int exitCode = tesseract.waitFor();
        if (exitCode != 0) {
            String err = new String(tesseract.getInputStream().readAllBytes());
            throw new RuntimeException("Tesseract OCR failed: " + err);
        }

        File txtFile = new File(outputBase + ".txt");
        String text = Files.readString(txtFile.toPath());

        tempPdf.delete();
        tempPng.delete();
        txtFile.delete();
        tempTxtBase.delete();

        return text;
    }
}
