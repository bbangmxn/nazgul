package com.nazgul.optimization.adapter.out.persistence.mapper;

import com.nazgul.optimization.adapter.out.persistence.entity.RecommendationTraceEntity;
import com.nazgul.optimization.domain.model.RecommendationTrace;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class RecommendationTracePersistenceMapper {

    public RecommendationTrace toDomain(RecommendationTraceEntity entity) {
        return RecommendationTrace.builder()
                .id(UUID.fromString(entity.getId()))
                .userId(entity.getUserId())
                .requestType(entity.getRequestType())
                .query(entity.getQuery())
                .contextPostId(entity.getContextPostId())
                .contextHobbyId(entity.getContextHobbyId())
                .selectedCardIds(entity.getSelectedCardIds() == null ? List.of() : entity.getSelectedCardIds().stream().map(UUID::fromString).toList())
                .selectedChunkIds(entity.getSelectedChunkIds() == null ? List.of() : entity.getSelectedChunkIds().stream().map(UUID::fromString).toList())
                .recommendedTargetIds(entity.getRecommendedTargetIds() == null ? List.of() : List.copyOf(entity.getRecommendedTargetIds()))
                .totalHits(entity.getTotalHits())
                .latencyMs(entity.getLatencyMs())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public RecommendationTraceEntity toEntity(RecommendationTrace trace) {
        return RecommendationTraceEntity.builder()
                .id(trace.getId().toString())
                .userId(trace.getUserId())
                .requestType(trace.getRequestType())
                .query(trace.getQuery())
                .contextPostId(trace.getContextPostId())
                .contextHobbyId(trace.getContextHobbyId())
                .selectedCardIds(trace.getSelectedCardIds() == null ? List.of() : trace.getSelectedCardIds().stream().map(UUID::toString).toList())
                .selectedChunkIds(trace.getSelectedChunkIds() == null ? List.of() : trace.getSelectedChunkIds().stream().map(UUID::toString).toList())
                .recommendedTargetIds(trace.getRecommendedTargetIds() == null ? List.of() : List.copyOf(trace.getRecommendedTargetIds()))
                .totalHits(trace.getTotalHits())
                .latencyMs(trace.getLatencyMs())
                .createdAt(trace.getCreatedAt())
                .build();
    }
}
