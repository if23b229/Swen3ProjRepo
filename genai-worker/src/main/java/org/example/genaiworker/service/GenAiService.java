package org.example.genaiworker.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenAiService {

    private final Client client;

    public GenAiService() {
        // API-Key wird automatisch aus GEMINI_API_KEY gezogen
        this.client = new Client();
    }

    public String generateSummary(String ocrText) {
        if (ocrText == null || ocrText.isBlank()) {
            return "Keine Inhalte erkannt.";
        }

        String prompt = """
                Erstelle eine kurze, präzise Zusammenfassung des folgenden
                OCR-Dokuments. Schreibe sachlich, kompakt und auf Deutsch:

                """ + ocrText;

        try {
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-flash-lite",
                    prompt,
                    GenerateContentConfig.builder()
                            .temperature(0.2f)
                            .maxOutputTokens(200)
                            .build()
            );

            String result = response.text();
            return (result == null || result.isBlank())
                    ? "Keine Zusammenfassung möglich."
                    : result.trim();

        } catch (Exception e) {
            log.error("Gemini 2.0 Flash API Fehler", e);
            return "Fehler bei der Zusammenfassung (Gemini API).";
        }
    }
}
