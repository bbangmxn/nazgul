package com.nazgul.optimization.application.port.in;

import java.util.List;
import java.util.UUID;

public record RecordRecommendationTraceCommand(
        Long userId,
        String requestType,
        String query,
        Long contextPostId,
        Long contextHobbyId,
        List<UUID> selectedCardIds,
        List<UUID> selectedChunkIds,
        List<Long> recommendedTargetIds,
        Integer totalHits,
        Long latencyMs
) {
}
