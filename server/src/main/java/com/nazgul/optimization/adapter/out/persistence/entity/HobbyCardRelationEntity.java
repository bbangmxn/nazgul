package com.nazgul.optimization.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "hobby_card_relations",
        indexes = {
                @Index(name = "idx_hobby_rel_source", columnList = "sourceCardId, updatedAt"),
                @Index(name = "idx_hobby_rel_target", columnList = "targetCardId")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HobbyCardRelationEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String sourceCardId;

    @Column(nullable = false, length = 36)
    private String targetCardId;

    @Column(nullable = false, length = 40)
    private String relationType;

    private Double weight;

    @Column(length = 36)
    private String evidenceChunkId;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
