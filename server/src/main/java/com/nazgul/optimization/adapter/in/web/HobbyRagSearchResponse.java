package com.nazgul.optimization.adapter.in.web;

import com.nazgul.optimization.application.port.in.SearchHobbyRagResult;
import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HobbyRagSearchResponse(
        String query,
        String answer,
        List<Long> recommendedPostIds,
        List<CardItem> cards,
        List<ChunkItem> chunks,
        UUID traceId
) {

    public static HobbyRagSearchResponse from(SearchHobbyRagResult result) {
        return new HobbyRagSearchResponse(
                result.query(),
                result.answer(),
                result.recommendedPostIds(),
                result.cards().stream().map(CardItem::from).toList(),
                result.chunks().stream().map(ChunkItem::from).toList(),
                result.traceId()
        );
    }

    public record CardItem(
            UUID id,
            Long hobbyId,
            String sourceType,
            Long sourceId,
            String type,
            String title,
            String summary,
            List<String> tags,
            Double trustScore,
            LocalDateTime updatedAt
    ) {
        private static CardItem from(HobbyCard card) {
            return new CardItem(
                    card.getId(),
                    card.getHobbyId(),
                    card.getSourceType(),
                    card.getSourceId(),
                    card.getType(),
                    card.getTitle(),
                    card.getSummary(),
                    card.getTags(),
                    card.getTrustScore(),
                    card.getUpdatedAt()
            );
        }
    }

    public record ChunkItem(
            UUID id,
            String sourceType,
            Long sourceId,
            Integer chunkIndex,
            String text,
            Integer tokenCount
    ) {
        private static ChunkItem from(ContentChunk chunk) {
            return new ChunkItem(
                    chunk.getId(),
                    chunk.getSourceType(),
                    chunk.getSourceId(),
                    chunk.getChunkIndex(),
                    chunk.getText(),
                    chunk.getTokenCount()
            );
        }
    }
}
