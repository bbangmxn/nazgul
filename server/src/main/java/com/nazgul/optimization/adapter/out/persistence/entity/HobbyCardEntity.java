package com.nazgul.optimization.adapter.out.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "hobby_cards",
        indexes = {
                @Index(name = "idx_hobby_cards_hobby_updated", columnList = "hobbyId, updatedAt"),
                @Index(name = "idx_hobby_cards_source", columnList = "sourceType, sourceId")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HobbyCardEntity {

    @Id
    @Column(length = 36)
    private String id;

    private Long hobbyId;

    @Column(nullable = false, length = 30)
    private String sourceType;

    @Column(nullable = false)
    private Long sourceId;

    @Column(length = 36)
    private String primaryChunkId;

    @Column(nullable = false, length = 40)
    private String type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 1000)
    private String summary;

    @Lob
    private String content;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hobby_card_claims", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "claim", nullable = false, length = 1000)
    @Builder.Default
    private List<String> claims = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hobby_card_tags", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "tag", nullable = false, length = 100)
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "hobby_card_evidence_chunks", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "chunk_id", nullable = false, length = 36)
    @Builder.Default
    private List<String> evidenceChunkIds = new ArrayList<>();

    @Column(nullable = false, length = 20)
    private String versionLabel;

    private Integer tokenSize;

    private Double trustScore;

    @Column(length = 120)
    private String scopeKey;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
