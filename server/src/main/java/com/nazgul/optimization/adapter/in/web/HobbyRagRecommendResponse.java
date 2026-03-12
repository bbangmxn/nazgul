package com.nazgul.optimization.adapter.in.web;

import com.nazgul.optimization.application.port.in.RecommendPostsResult;
import java.util.List;

public record HobbyRagRecommendResponse(
        Long userId,
        Long hobbyId,
        String query,
        List<Long> recommendedPostIds
) {

    public static HobbyRagRecommendResponse from(RecommendPostsResult result) {
        return new HobbyRagRecommendResponse(
                result.userId(),
                result.hobbyId(),
                result.query(),
                result.recommendedPostIds()
        );
    }
}
