package com.nazgul.optimization.adapter.out.persistence.mapper;

import com.nazgul.optimization.adapter.out.persistence.entity.HobbyCardRelationEntity;
import com.nazgul.optimization.domain.model.HobbyCardRelation;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class HobbyCardRelationPersistenceMapper {

    public HobbyCardRelation toDomain(HobbyCardRelationEntity entity) {
        return HobbyCardRelation.builder()
                .id(UUID.fromString(entity.getId()))
                .sourceCardId(UUID.fromString(entity.getSourceCardId()))
                .targetCardId(UUID.fromString(entity.getTargetCardId()))
                .relationType(entity.getRelationType())
                .weight(entity.getWeight())
                .evidenceChunkId(entity.getEvidenceChunkId() == null ? null : UUID.fromString(entity.getEvidenceChunkId()))
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public HobbyCardRelationEntity toEntity(HobbyCardRelation relation) {
        return HobbyCardRelationEntity.builder()
                .id(relation.getId().toString())
                .sourceCardId(relation.getSourceCardId().toString())
                .targetCardId(relation.getTargetCardId().toString())
                .relationType(relation.getRelationType())
                .weight(relation.getWeight())
                .evidenceChunkId(relation.getEvidenceChunkId() == null ? null : relation.getEvidenceChunkId().toString())
                .updatedAt(relation.getUpdatedAt())
                .build();
    }
}
