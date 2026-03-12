package com.nazgul.optimization.application.port.in;

public record HobbyRagBenchmarkQuery(
        Long userId,
        Long hobbyId,
        int iterations
) {
}
