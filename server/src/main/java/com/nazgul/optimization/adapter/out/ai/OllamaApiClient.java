package com.nazgul.optimization.adapter.out.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class OllamaApiClient {

    private final ObjectMapper objectMapper;

    @Value("${app.rag.ollama.enabled:true}")
    private boolean enabled;

    @Value("${app.rag.ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${app.rag.ollama.chat-model:qwen3:8b}")
    private String chatModel;

    @Value("${app.rag.ollama.embedding-model:nomic-embed-text:latest}")
    private String embeddingModel;

    @Value("${app.rag.ollama.timeout-ms:60000}")
    private long timeoutMs;

    public boolean isEnabled() {
        return enabled;
    }

    public String getChatModel() {
        return chatModel;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public String chat(String prompt) {
        if (!enabled || !StringUtils.hasText(prompt)) {
            return null;
        }

        try {
            JsonNode body = objectMapper.createObjectNode()
                    .put("model", chatModel)
                    .put("stream", false)
                    .set("messages", objectMapper.createArrayNode().add(
                            objectMapper.createObjectNode()
                                    .put("role", "user")
                                    .put("content", prompt)
                    ));

            JsonNode response = post("/api/chat", body);
            JsonNode content = response.path("message").path("content");
            return content.isTextual() ? content.asText() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public List<List<Double>> embed(List<String> inputs) {
        if (!enabled || inputs == null || inputs.isEmpty()) {
            return List.of();
        }

        try {
            JsonNode body = objectMapper.createObjectNode()
                    .put("model", embeddingModel)
                    .set("input", objectMapper.valueToTree(inputs));

            JsonNode response = post("/api/embed", body);
            JsonNode embeddingsNode = response.path("embeddings");
            if (!embeddingsNode.isArray()) {
                return List.of();
            }

            List<List<Double>> embeddings = new ArrayList<>();
            for (JsonNode embeddingNode : embeddingsNode) {
                List<Double> vector = new ArrayList<>();
                for (JsonNode value : embeddingNode) {
                    vector.add(value.asDouble());
                }
                embeddings.add(vector);
            }
            return embeddings;
        } catch (Exception ignored) {
            return List.of();
        }
    }

    private JsonNode post(String path, JsonNode body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeoutMs))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofMillis(timeoutMs))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new IOException("ollama request failed: " + response.statusCode());
        }
        return objectMapper.readTree(response.body());
    }
}
