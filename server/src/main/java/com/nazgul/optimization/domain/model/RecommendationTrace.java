package com.nazgul.optimization.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationTrace {

    private UUID id;
    private Long userId;
    private String requestType;
    private String query;
    private Long contextPostId;
    private Long contextHobbyId;
    private List<UUID> selectedCardIds;
    private List<UUID> selectedChunkIds;
    private List<Long> recommendedTargetIds;
    private Integer totalHits;
    private Long latencyMs;
    private LocalDateTime createdAt;
}
