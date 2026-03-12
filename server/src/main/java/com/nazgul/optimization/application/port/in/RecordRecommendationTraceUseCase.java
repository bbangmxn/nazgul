package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.RecommendationTrace;

public interface RecordRecommendationTraceUseCase {

    RecommendationTrace recordTrace(RecordRecommendationTraceCommand command);
}
