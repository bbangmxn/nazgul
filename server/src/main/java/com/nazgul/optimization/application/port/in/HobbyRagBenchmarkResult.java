package com.nazgul.optimization.application.port.in;

import java.util.List;

public record HobbyRagBenchmarkResult(
        String provider,
        String chatModel,
        String embeddingModel,
        int iterations,
        long totalLatencyMs,
        double averageLatencyMs,
        List<HobbyRagBenchmarkItem> items
) {
}
