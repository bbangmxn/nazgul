package com.nazgul.rag.adapter.out.persistence.mapper;

import com.nazgul.rag.adapter.out.persistence.entity.EvidenceRefEmbeddable;
import com.nazgul.rag.adapter.out.persistence.entity.RagCardJpaEntity;
import com.nazgul.rag.adapter.out.persistence.entity.RagChapterJpaEntity;
import com.nazgul.rag.adapter.out.persistence.entity.RagChunkJpaEntity;
import com.nazgul.rag.adapter.out.persistence.entity.RagDocumentJpaEntity;
import com.nazgul.rag.adapter.out.persistence.entity.RagQueryTraceJpaEntity;
import com.nazgul.rag.domain.entity.RagCard;
import com.nazgul.rag.domain.entity.RagChapter;
import com.nazgul.rag.domain.entity.RagChunk;
import com.nazgul.rag.domain.entity.RagDocument;
import com.nazgul.rag.domain.entity.RagQueryTrace;
import com.nazgul.rag.domain.vo.EvidenceRef;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RagPersistenceMapper {

    public RagCard toDomain(RagCardJpaEntity entity) {
        return RagCard.builder()
                .id(entity.getId())
                .documentId(entity.getDocumentId())
                .chapterId(entity.getChapterId())
                .primaryChunkId(entity.getPrimaryChunkId())
                .type(entity.getType())
                .title(entity.getTitle())
                .summary(entity.getSummary())
                .claims(copyStrings(entity.getClaims()))
                .tags(copyStrings(entity.getTags()))
                .evidenceRefs(entity.getEvidenceRefs() == null
                        ? List.of()
                        : entity.getEvidenceRefs().stream().map(this::toDomain).toList())
                .versionLabel(entity.getVersionLabel())
                .trustScore(entity.getTrustScore())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RagChunk toDomain(RagChunkJpaEntity entity) {
        return RagChunk.builder()
                .id(entity.getId())
                .documentId(entity.getDocumentId())
                .chapterId(entity.getChapterId())
                .chunkIndex(entity.getChunkIndex())
                .text(entity.getText())
                .tokenCount(entity.getTokenCount())
                .startOffset(entity.getStartOffset())
                .endOffset(entity.getEndOffset())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RagDocument toDomain(RagDocumentJpaEntity entity) {
        return RagDocument.builder()
                .id(entity.getId())
                .sourceKey(entity.getSourceKey())
                .sourceType(entity.getSourceType())
                .title(entity.getTitle())
                .contentHash(entity.getContentHash())
                .indexedAt(entity.getIndexedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RagChapter toDomain(RagChapterJpaEntity entity) {
        return RagChapter.builder()
                .id(entity.getId())
                .documentId(entity.getDocumentId())
                .chapterIndex(entity.getChapterIndex())
                .title(entity.getTitle())
                .sectionPath(entity.getSectionPath())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public RagQueryTraceJpaEntity toJpa(RagQueryTrace trace) {
        return RagQueryTraceJpaEntity.builder()
                .id(trace.getId())
                .query(trace.getQuery())
                .scopeDocumentIds(copyUuids(trace.getScopeDocumentIds()))
                .scopeChapterIds(copyUuids(trace.getScopeChapterIds()))
                .selectedCardIds(copyUuids(trace.getSelectedCardIds()))
                .selectedChunkIds(copyUuids(trace.getSelectedChunkIds()))
                .totalHits(trace.getTotalHits())
                .latencyMs(trace.getLatencyMs())
                .createdAt(trace.getCreatedAt())
                .build();
    }

    private EvidenceRef toDomain(EvidenceRefEmbeddable embeddable) {
        return new EvidenceRef(
                embeddable.getChunkId(),
                embeddable.getLocator(),
                embeddable.getPriority()
        );
    }

    private List<String> copyStrings(List<String> values) {
        return values == null ? List.of() : List.copyOf(values);
    }

    private List<UUID> copyUuids(List<UUID> values) {
        return values == null ? List.of() : List.copyOf(values);
    }
}
