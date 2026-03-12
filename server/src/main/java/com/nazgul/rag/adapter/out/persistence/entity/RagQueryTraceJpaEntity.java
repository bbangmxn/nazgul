package com.nazgul.rag.adapter.out.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "rag_query_traces")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagQueryTraceJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 500)
    private String query;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_trace_scope_documents", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "document_id", nullable = false)
    private List<UUID> scopeDocumentIds = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_trace_scope_chapters", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "chapter_id", nullable = false)
    private List<UUID> scopeChapterIds = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_trace_selected_cards", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "card_id", nullable = false)
    private List<UUID> selectedCardIds = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_trace_selected_chunks", joinColumns = @JoinColumn(name = "trace_id"))
    @Column(name = "chunk_id", nullable = false)
    private List<UUID> selectedChunkIds = new ArrayList<>();

    @Column(name = "total_hits", nullable = false)
    private Integer totalHits;

    @Column(name = "latency_ms", nullable = false)
    private Long latencyMs;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
