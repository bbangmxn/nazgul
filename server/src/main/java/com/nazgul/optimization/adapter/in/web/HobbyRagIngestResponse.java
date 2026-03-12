package com.nazgul.optimization.adapter.in.web;

import com.nazgul.optimization.application.port.in.IngestHobbyContentResult;
import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record HobbyRagIngestResponse(
        CardItem card,
        List<ChunkItem> chunks
) {

    public static HobbyRagIngestResponse from(IngestHobbyContentResult result) {
        return new HobbyRagIngestResponse(
                CardItem.from(result.card()),
                result.chunks().stream().map(ChunkItem::from).toList()
        );
    }

    public record CardItem(
            UUID id,
            Long hobbyId,
            String sourceType,
            Long sourceId,
            UUID primaryChunkId,
            String type,
            String title,
            String summary,
            List<String> claims,
            List<String> tags,
            List<UUID> evidenceChunkIds,
            String versionLabel,
            Integer tokenSize,
            Double trustScore,
            String scopeKey,
            LocalDateTime updatedAt
    ) {
        private static CardItem from(HobbyCard card) {
            return new CardItem(
                    card.getId(),
                    card.getHobbyId(),
                    card.getSourceType(),
                    card.getSourceId(),
                    card.getPrimaryChunkId(),
                    card.getType(),
                    card.getTitle(),
                    card.getSummary(),
                    card.getClaims(),
                    card.getTags(),
                    card.getEvidenceChunkIds(),
                    card.getVersionLabel(),
                    card.getTokenSize(),
                    card.getTrustScore(),
                    card.getScopeKey(),
                    card.getUpdatedAt()
            );
        }
    }

    public record ChunkItem(
            UUID id,
            String sourceType,
            Long sourceId,
            Long hobbyId,
            Integer chunkIndex,
            String text,
            Integer tokenCount,
            Integer startOffset,
            Integer endOffset,
            String contentHash,
            LocalDateTime updatedAt
    ) {
        private static ChunkItem from(ContentChunk chunk) {
            return new ChunkItem(
                    chunk.getId(),
                    chunk.getSourceType(),
                    chunk.getSourceId(),
                    chunk.getHobbyId(),
                    chunk.getChunkIndex(),
                    chunk.getText(),
                    chunk.getTokenCount(),
                    chunk.getStartOffset(),
                    chunk.getEndOffset(),
                    chunk.getContentHash(),
                    chunk.getUpdatedAt()
            );
        }
    }
}
