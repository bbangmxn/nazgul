package com.nazgul.optimization.adapter.out.ai;

import com.nazgul.optimization.application.port.out.HobbyRagRerankPort;
import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class OllamaHobbyRagRerankAdapter implements HobbyRagRerankPort {

    private final OllamaApiClient ollamaApiClient;

    @Value("${app.rag.performance.fast-mode:false}")
    private boolean fastMode;

    @Value("${app.rag.performance.rerank-enabled:true}")
    private boolean rerankEnabled;

    @Override
    public List<HobbyCard> rerankCards(String query, List<HobbyCard> candidates, int limit) {
        if (fastMode || !rerankEnabled) {
            return candidates == null ? List.of() : candidates.stream().limit(limit).toList();
        }

        if (!StringUtils.hasText(query) || candidates == null || candidates.isEmpty()) {
            return candidates == null ? List.of() : candidates.stream().limit(limit).toList();
        }

        List<String> payload = new ArrayList<>();
        payload.add(query);
        payload.addAll(candidates.stream().map(this::renderCard).toList());

        List<List<Double>> embeddings = ollamaApiClient.embed(payload);
        if (embeddings.size() != payload.size()) {
            return candidates.stream().limit(limit).toList();
        }

        List<Double> queryVector = embeddings.get(0);
        return java.util.stream.IntStream.range(0, candidates.size())
                .mapToObj(index -> new ScoredCard(candidates.get(index), cosine(queryVector, embeddings.get(index + 1))))
                .sorted(Comparator.comparingDouble(ScoredCard::score).reversed())
                .limit(limit)
                .map(ScoredCard::card)
                .toList();
    }

    @Override
    public List<ContentChunk> rerankChunks(String query, List<ContentChunk> candidates, int limit) {
        if (fastMode || !rerankEnabled) {
            return candidates == null ? List.of() : candidates.stream().limit(limit).toList();
        }

        if (!StringUtils.hasText(query) || candidates == null || candidates.isEmpty()) {
            return candidates == null ? List.of() : candidates.stream().limit(limit).toList();
        }

        List<String> payload = new ArrayList<>();
        payload.add(query);
        payload.addAll(candidates.stream().map(ContentChunk::getText).toList());

        List<List<Double>> embeddings = ollamaApiClient.embed(payload);
        if (embeddings.size() != payload.size()) {
            return candidates.stream().limit(limit).toList();
        }

        List<Double> queryVector = embeddings.get(0);
        return java.util.stream.IntStream.range(0, candidates.size())
                .mapToObj(index -> new ScoredChunk(candidates.get(index), cosine(queryVector, embeddings.get(index + 1))))
                .sorted(Comparator.comparingDouble(ScoredChunk::score).reversed())
                .limit(limit)
                .map(ScoredChunk::chunk)
                .toList();
    }

    private String renderCard(HobbyCard card) {
        return String.join(" ",
                safe(card.getTitle()),
                safe(card.getSummary()),
                safe(card.getContent()),
                card.getClaims() == null ? "" : String.join(" ", card.getClaims()),
                card.getTags() == null ? "" : String.join(" ", card.getTags())
        ).trim();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private double cosine(List<Double> a, List<Double> b) {
        if (a == null || b == null || a.size() != b.size() || a.isEmpty()) {
            return 0.0d;
        }
        double dot = 0.0d;
        double normA = 0.0d;
        double normB = 0.0d;
        for (int i = 0; i < a.size(); i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        if (normA == 0.0d || normB == 0.0d) {
            return 0.0d;
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private record ScoredCard(HobbyCard card, double score) {
    }

    private record ScoredChunk(ContentChunk chunk, double score) {
    }
}
