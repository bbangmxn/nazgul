package com.nazgul.optimization.application.port.in;

public record RecommendPostsQuery(
        Long userId,
        Long hobbyId,
        String query,
        Long contextPostId,
        int limit
) {
}
