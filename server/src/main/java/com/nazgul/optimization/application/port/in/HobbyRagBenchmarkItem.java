package com.nazgul.optimization.application.port.in;

import java.util.List;

public record HobbyRagBenchmarkItem(
        String query,
        long latencyMs,
        int cardCount,
        int chunkCount,
        List<Long> recommendedPostIds,
        String answerPreview
) {
}
