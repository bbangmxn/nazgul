package com.nazgul.rag.adapter.out.persistence.entity;

import com.nazgul.rag.domain.enums.RagDocumentSourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "rag_documents")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagDocumentJpaEntity {

    @Id
    private UUID id;

    @Column(name = "source_key", nullable = false, length = 255)
    private String sourceKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type", nullable = false, length = 30)
    private RagDocumentSourceType sourceType;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "content_hash", length = 255)
    private String contentHash;

    @Column(name = "indexed_at")
    private LocalDateTime indexedAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
