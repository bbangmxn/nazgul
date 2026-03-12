package com.nazgul.optimization.adapter.out.persistence;

import com.nazgul.optimization.adapter.out.persistence.mapper.RecommendationTracePersistenceMapper;
import com.nazgul.optimization.adapter.out.persistence.repository.RecommendationTraceJpaRepository;
import com.nazgul.optimization.application.port.out.RecommendationTraceStore;
import com.nazgul.optimization.domain.model.RecommendationTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class RecommendationTracePersistenceAdapter implements RecommendationTraceStore {

    private final RecommendationTraceJpaRepository recommendationTraceJpaRepository;
    private final RecommendationTracePersistenceMapper recommendationTracePersistenceMapper;

    @Override
    public RecommendationTrace save(RecommendationTrace trace) {
        return recommendationTracePersistenceMapper.toDomain(
                recommendationTraceJpaRepository.save(recommendationTracePersistenceMapper.toEntity(trace))
        );
    }
}
