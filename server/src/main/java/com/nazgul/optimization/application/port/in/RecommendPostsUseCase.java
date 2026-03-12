package com.nazgul.optimization.application.port.in;

public interface RecommendPostsUseCase {

    RecommendPostsResult recommend(RecommendPostsQuery query);
}
