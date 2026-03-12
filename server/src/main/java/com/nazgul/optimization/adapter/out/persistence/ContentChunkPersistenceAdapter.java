package com.nazgul.optimization.adapter.out.persistence;

import com.nazgul.optimization.adapter.out.persistence.entity.ContentChunkEntity;
import com.nazgul.optimization.adapter.out.persistence.mapper.ContentChunkPersistenceMapper;
import com.nazgul.optimization.adapter.out.persistence.repository.ContentChunkJpaRepository;
import com.nazgul.optimization.application.port.out.ContentChunkStore;
import com.nazgul.optimization.domain.model.ContentChunk;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class ContentChunkPersistenceAdapter implements ContentChunkStore {

    private final ContentChunkJpaRepository contentChunkJpaRepository;
    private final ContentChunkPersistenceMapper contentChunkPersistenceMapper;

    @Override
    public ContentChunk save(ContentChunk chunk) {
        return contentChunkPersistenceMapper.toDomain(
                contentChunkJpaRepository.save(contentChunkPersistenceMapper.toEntity(chunk))
        );
    }

    @Override
    public List<ContentChunk> search(String query, Long hobbyId, Collection<Long> sourceIds, int limit) {
        boolean filterBySource = sourceIds != null && !sourceIds.isEmpty();
        String normalizedQuery = query == null ? "" : query.toLowerCase();

        List<ContentChunkEntity> candidates = resolveCandidates(hobbyId, sourceIds, filterBySource);
        return candidates.stream()
                .map(contentChunkPersistenceMapper::toDomain)
                .filter(chunk -> normalizedQuery.isBlank() || containsIgnoreCase(chunk.getText(), normalizedQuery))
                .sorted(Comparator
                        .comparingInt((ContentChunk chunk) -> score(chunk, normalizedQuery)).reversed()
                        .thenComparing(ContentChunk::getUpdatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .toList();
    }

    private List<ContentChunkEntity> resolveCandidates(Long hobbyId, Collection<Long> sourceIds, boolean filterBySource) {
        if (hobbyId != null && filterBySource) {
            return contentChunkJpaRepository.findByHobbyIdAndSourceIdInOrderByUpdatedAtDesc(hobbyId, sourceIds);
        }
        if (hobbyId != null) {
            return contentChunkJpaRepository.findByHobbyIdOrderByUpdatedAtDesc(hobbyId);
        }
        if (filterBySource) {
            return contentChunkJpaRepository.findBySourceIdInOrderByUpdatedAtDesc(sourceIds);
        }
        return contentChunkJpaRepository.findAllByOrderByUpdatedAtDesc();
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
