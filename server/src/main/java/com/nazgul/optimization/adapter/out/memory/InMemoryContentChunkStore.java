package com.nazgul.optimization.adapter.out.memory;

import com.nazgul.optimization.application.port.out.ContentChunkStore;
import com.nazgul.optimization.domain.model.ContentChunk;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class InMemoryContentChunkStore implements ContentChunkStore {

    private final Map<java.util.UUID, ContentChunk> storage = new ConcurrentHashMap<>();

    @Override
    public ContentChunk save(ContentChunk chunk) {
        storage.put(chunk.getId(), chunk);
        return chunk;
    }

    @Override
    public List<ContentChunk> search(String query, Long hobbyId, Collection<Long> sourceIds, int limit) {
        String normalizedQuery = query == null ? "" : query.toLowerCase();
        boolean filterBySource = sourceIds != null && !sourceIds.isEmpty();

        return storage.values().stream()
                .filter(chunk -> hobbyId == null || hobbyId.equals(chunk.getHobbyId()))
                .filter(chunk -> !filterBySource || sourceIds.contains(chunk.getSourceId()))
                .filter(chunk -> normalizedQuery.isBlank() || containsIgnoreCase(chunk.getText(), normalizedQuery))
                .sorted(Comparator
                        .comparingInt((ContentChunk chunk) -> score(chunk, normalizedQuery)).reversed()
                        .thenComparing(ContentChunk::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }

    private int score(ContentChunk chunk, String normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            return 0;
        }

        return keywordScore(chunk.getText(), normalizedQuery);
    }

    private int keywordScore(String text, String normalizedQuery) {
        if (text == null || normalizedQuery.isBlank()) {
            return 0;
        }

        String normalizedText = text.toLowerCase();
        int score = 0;
        for (String token : normalizedQuery.split("\\s+")) {
            if (!token.isBlank() && normalizedText.contains(token)) {
                score += 2;
            }
        }
        return score;
    }

    private boolean containsIgnoreCase(String text, String query) {
        return text != null && text.toLowerCase().contains(query);
    }
}
