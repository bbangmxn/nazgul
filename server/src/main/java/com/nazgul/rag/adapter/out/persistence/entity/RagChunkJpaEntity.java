package com.nazgul.rag.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@Table(name = "rag_chunks")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagChunkJpaEntity {

    @Id
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chapter_id", nullable = false)
    private UUID chapterId;

    @Column(name = "chunk_index", nullable = false)
    private Integer chunkIndex;

    @Column(nullable = false, length = 8000)
    private String text;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "start_offset")
    private Integer startOffset;

    @Column(name = "end_offset")
    private Integer endOffset;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
