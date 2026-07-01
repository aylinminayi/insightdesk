package insightdesk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiAnalyzerService { // Gemini API'sini kullanarak müşteri geri bildirimlerini analiz eden servis

    private static final String MODEL_NAME = "gemini-3.1-flash-lite";

    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GeminiAnalyzerService( // API anahtarını yapılandırmadan alır
            @Value("${GEMINI_API_KEY:}") String apiKey
    ) {
        this.apiKey = apiKey;
    }

    public void analyze(Feedback feedback) { // Geri bildirimi analiz eder ve sonuçları geri bildirime kaydeder
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("GEMINI_API_KEY bulunamadı.");
        }

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        String prompt = """
                Analyze this customer-support feedback.

                Return ONLY valid JSON.
                Do not use markdown.
                Do not add any explanation.

                Use exactly this format:
                {
                  "sentiment": "positive|neutral|negative",
                  "category": "bug|feature_request|complaint|question",
                  "urgency": "low|medium|high",
                  "summary": "one short sentence",
                  "suggested_reply": "short professional reply"
                }

                Feedback title:
                %s

                Feedback body:
                %s
                """.formatted(feedback.getTitle(), feedback.getBody());

        GenerateContentResponse response = client.models.generateContent( 
                MODEL_NAME,
                prompt,
                null
        );

        String json = response.text();

        if (json == null || json.isBlank()) {
            throw new IllegalStateException("Gemini boş yanıt döndürdü.");
        }

        try {
            JsonNode result = objectMapper.readTree(json);

            String sentiment = requiredValue(result, "sentiment");
            String category = requiredValue(result, "category");
            String urgency = requiredValue(result, "urgency");
            String summary = requiredValue(result, "summary");
            String suggestedReply = requiredValue(result, "suggested_reply");

            validateAllowedValue(
                    sentiment,
                    "sentiment",
                    "positive",
                    "neutral",
                    "negative"
            );

            validateAllowedValue(
                    category,
                    "category",
                    "bug",
                    "feature_request",
                    "complaint",
                    "question"
            );

            validateAllowedValue(
                    urgency,
                    "urgency",
                    "low",
                    "medium",
                    "high"
            );

            feedback.setAnalysis(
                    sentiment,
                    category,
                    urgency,
                    summary,
                    suggestedReply,
                    MODEL_NAME
            );

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Gemini JSON çıktısı okunamadı: " + json, 
                    exception
            );
        }
    }

    private String requiredValue(JsonNode result, String fieldName) {
        JsonNode field = result.get(fieldName);

        if (field == null || field.asText().isBlank()) {
            throw new IllegalArgumentException(
                    "Gemini JSON alanı eksik veya boş: " + fieldName
            );
        }

        return field.asText().trim();
    }

    private void validateAllowedValue(
            String value,
            String fieldName,
            String... allowedValues
    ) {
        for (String allowedValue : allowedValues) {
            if (allowedValue.equals(value)) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "Geçersiz " + fieldName + " değeri: " + value
        );
    }
}