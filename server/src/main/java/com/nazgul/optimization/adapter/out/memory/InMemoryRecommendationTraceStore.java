package com.nazgul.optimization.adapter.out.memory;

import com.nazgul.optimization.application.port.out.RecommendationTraceStore;
import com.nazgul.optimization.domain.model.RecommendationTrace;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
public class InMemoryRecommendationTraceStore implements RecommendationTraceStore {

    private final Map<UUID, RecommendationTrace> storage = new ConcurrentHashMap<>();

    @Override
    public RecommendationTrace save(RecommendationTrace trace) {
        storage.put(trace.getId(), trace);
        return trace;
    }
}
