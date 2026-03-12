package com.nazgul.rag.adapter.in.web;

import com.nazgul.rag.application.port.in.SearchRagResult;
import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.vo.EvidenceRef;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RagSearchResponse(
        String query,
        List<UUID> scopeDocumentIds,
        List<UUID> scopeChapterIds,
        List<CardItem> cards,
        List<ChunkItem> chunks
) {

    public static RagSearchResponse from(SearchRagResult result) {
        return new RagSearchResponse(
                result.query(),
                result.scope().documentIds().stream().toList(),
                result.scope().chapterIds().stream().toList(),
                result.cards().stream().map(CardItem::from).toList(),
                result.chunks().stream().map(ChunkItem::from).toList()
        );
    }

    public record CardItem(
            UUID id,
            UUID documentId,
            UUID chapterId,
            UUID primaryChunkId,
            String type,
            String title,
            String summary,
            List<String> claims,
            List<String> tags,
            List<EvidenceItem> evidenceRefs,
            String versionLabel,
            Double trustScore,
            LocalDateTime updatedAt
    ) {

        private static CardItem from(RagCard card) {
            return new CardItem(
                    card.getId(),
                    card.getDocumentId(),
                    card.getChapterId(),
                    card.getPrimaryChunkId(),
                    card.getType() == null ? null : card.getType().name(),
                    card.getTitle(),
                    card.getSummary(),
                    card.getClaims(),
                    card.getTags(),
                    card.getEvidenceRefs() == null ? List.of() : card.getEvidenceRefs().stream().map(EvidenceItem::from).toList(),
                    card.getVersionLabel(),
                    card.getTrustScore(),
                    card.getUpdatedAt()
            );
        }
    }

    public record ChunkItem(
            UUID id,
            UUID documentId,
            UUID chapterId,
            Integer chunkIndex,
            String text,
            Integer tokenCount,
            Integer startOffset,
            Integer endOffset,
            LocalDateTime updatedAt
    ) {

        private static ChunkItem from(RagChunk chunk) {
            return new ChunkItem(
                    chunk.getId(),
                    chunk.getDocumentId(),
                    chunk.getChapterId(),
                    chunk.getChunkIndex(),
                    chunk.getText(),
                    chunk.getTokenCount(),
                    chunk.getStartOffset(),
                    chunk.getEndOffset(),
                    chunk.getUpdatedAt()
            );
        }
    }

    public record EvidenceItem(
            UUID chunkId,
            String locator,
            Integer priority
    ) {

        private static EvidenceItem from(EvidenceRef evidenceRef) {
            return new EvidenceItem(
                    evidenceRef.chunkId(),
                    evidenceRef.locator(),
                    evidenceRef.priority()
            );
        }
    }
}
