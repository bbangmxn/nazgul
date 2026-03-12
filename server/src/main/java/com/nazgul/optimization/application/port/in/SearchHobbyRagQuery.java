package com.nazgul.optimization.application.port.in;

public record SearchHobbyRagQuery(
        Long userId,
        Long hobbyId,
        Long contextPostId,
        String query,
        int limit
) {
}
