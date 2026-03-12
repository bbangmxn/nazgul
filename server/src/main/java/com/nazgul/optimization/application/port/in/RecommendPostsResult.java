package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;

public record RecommendPostsResult(
        Long userId,
        Long hobbyId,
        String query,
        List<Long> recommendedPostIds,
        List<HobbyCard> recommendedCards,
        List<ContentChunk> evidenceChunks
) {
}
