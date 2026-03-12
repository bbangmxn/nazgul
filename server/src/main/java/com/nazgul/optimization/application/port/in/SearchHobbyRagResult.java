package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.ContentChunk;
import com.nazgul.optimization.domain.model.HobbyCard;
import java.util.List;
import java.util.UUID;

public record SearchHobbyRagResult(
        String query,
        String answer,
        List<Long> recommendedPostIds,
        List<HobbyCard> cards,
        List<ContentChunk> chunks,
        UUID traceId
) {
}
