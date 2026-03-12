package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.RecommendationTrace;

public interface RecommendationTraceStore {

    RecommendationTrace save(RecommendationTrace trace);
}
