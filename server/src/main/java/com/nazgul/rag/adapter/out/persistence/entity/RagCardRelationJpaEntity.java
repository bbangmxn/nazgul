package com.nazgul.rag.adapter.out.persistence.entity;

import com.nazgul.rag.domain.enums.RagRelationType;
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
@Table(name = "rag_card_relations")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RagCardRelationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "source_card_id", nullable = false)
    private UUID sourceCardId;

    @Column(name = "target_card_id", nullable = false)
    private UUID targetCardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "relation_type", nullable = false, length = 30)
    private RagRelationType relationType;

    @Column(nullable = false)
    private Double weight;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
