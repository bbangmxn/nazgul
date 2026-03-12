package com.nazgul.optimization.adapter.out.persistence.mapper;

import com.nazgul.optimization.adapter.out.persistence.entity.HobbyCardEntity;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class HobbyCardPersistenceMapper {

    public HobbyCard toDomain(HobbyCardEntity entity) {
        return HobbyCard.builder()
                .id(UUID.fromString(entity.getId()))
                .hobbyId(entity.getHobbyId())
                .sourceType(entity.getSourceType())
                .sourceId(entity.getSourceId())
                .primaryChunkId(parseUuid(entity.getPrimaryChunkId()))
                .type(entity.getType())
                .title(entity.getTitle())
                .summary(entity.getSummary())
                .content(entity.getContent())
                .claims(entity.getClaims() == null ? List.of() : List.copyOf(entity.getClaims()))
                .tags(entity.getTags() == null ? List.of() : List.copyOf(entity.getTags()))
                .evidenceChunkIds(entity.getEvidenceChunkIds() == null ? List.of() : entity.getEvidenceChunkIds().stream().map(UUID::fromString).toList())
                .versionLabel(entity.getVersionLabel())
                .tokenSize(entity.getTokenSize())
                .trustScore(entity.getTrustScore())
                .scopeKey(entity.getScopeKey())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public HobbyCardEntity toEntity(HobbyCard card) {
        return HobbyCardEntity.builder()
                .id(card.getId().toString())
                .hobbyId(card.getHobbyId())
                .sourceType(card.getSourceType())
                .sourceId(card.getSourceId())
                .primaryChunkId(stringify(card.getPrimaryChunkId()))
                .type(card.getType())
                .title(card.getTitle())
                .summary(card.getSummary())
                .content(card.getContent())
                .claims(card.getClaims() == null ? List.of() : List.copyOf(card.getClaims()))
                .tags(card.getTags() == null ? List.of() : List.copyOf(card.getTags()))
                .evidenceChunkIds(card.getEvidenceChunkIds() == null ? List.of() : card.getEvidenceChunkIds().stream().map(UUID::toString).toList())
                .versionLabel(card.getVersionLabel())
                .tokenSize(card.getTokenSize())
                .trustScore(card.getTrustScore())
                .scopeKey(card.getScopeKey())
                .updatedAt(card.getUpdatedAt())
                .build();
    }

    private UUID parseUuid(String value) {
        return value == null ? null : UUID.fromString(value);
    }

    private String stringify(UUID value) {
        return value == null ? null : value.toString();
    }
}
