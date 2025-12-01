package com.swen3.paperless.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("genai")
@Service
public class GeminiService {

    @Value("${PP_GENAI_KEY}")
    private String apiKey;

    private final OkHttpClient client = new OkHttpClient();

    public String summarize(String text) throws Exception {

        // Build JSON request body
        JsonObject part = new JsonObject();
        part.addProperty("text", "Summarize the following text:\n" + text);

        JsonArray parts = new JsonArray();
        parts.add(part);

        JsonObject message = new JsonObject();
        message.add("parts", parts);

        JsonArray contents = new JsonArray();
        contents.add(message);

        JsonObject prompt = new JsonObject();
        prompt.add("contents", contents);

        RequestBody body = RequestBody.create(
                prompt.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new RuntimeException("Gemini API error: " + response);
            }

            String responseText = response.body().string();

            JsonObject json = com.google.gson.JsonParser.parseString(responseText).getAsJsonObject();

            String summary =
                    json.getAsJsonArray("candidates")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("content")
                            .getAsJsonArray("parts")
                            .get(0).getAsJsonObject()
                            .get("text").getAsString();

            return summary;
        }
    }
}
