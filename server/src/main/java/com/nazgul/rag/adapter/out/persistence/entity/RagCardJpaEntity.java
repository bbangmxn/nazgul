package com.nazgul.rag.adapter.out.persistence.entity;

import com.nazgul.rag.domain.enums.RagCardType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "rag_cards")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagCardJpaEntity {

    @Id
    private UUID id;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;

    @Column(name = "chapter_id", nullable = false)
    private UUID chapterId;

    @Column(name = "primary_chunk_id")
    private UUID primaryChunkId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RagCardType type;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String summary;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_card_claims", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "claim_text", nullable = false, length = 1000)
    private List<String> claims = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_card_tags", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "tag_value", nullable = false, length = 100)
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "rag_card_evidence_refs", joinColumns = @JoinColumn(name = "card_id"))
    private List<EvidenceRefEmbeddable> evidenceRefs = new ArrayList<>();

    @Column(name = "version_label", length = 100)
    private String versionLabel;

    @Column(name = "trust_score")
    private Double trustScore;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
