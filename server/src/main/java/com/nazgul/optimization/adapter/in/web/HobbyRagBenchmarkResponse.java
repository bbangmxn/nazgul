package com.nazgul.optimization.adapter.in.web;

import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkItem;
import com.nazgul.optimization.application.port.in.HobbyRagBenchmarkResult;
import java.util.List;

public record HobbyRagBenchmarkResponse(
        String provider,
        String chatModel,
        String embeddingModel,
        int iterations,
        long totalLatencyMs,
        double averageLatencyMs,
        List<HobbyRagBenchmarkItem> items
) {

    public static HobbyRagBenchmarkResponse from(HobbyRagBenchmarkResult result) {
        return new HobbyRagBenchmarkResponse(
                result.provider(),
                result.chatModel(),
                result.embeddingModel(),
                result.iterations(),
                result.totalLatencyMs(),
                result.averageLatencyMs(),
                result.items()
        );
    }
}
