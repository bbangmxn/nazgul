package com.nazgul.optimization.adapter.out.persistence;

import com.nazgul.optimization.adapter.out.persistence.mapper.OptimizationJobPersistenceMapper;
import com.nazgul.optimization.adapter.out.persistence.repository.OptimizationJobJpaRepository;
import com.nazgul.optimization.application.port.out.OptimizationJobStore;
import com.nazgul.optimization.domain.model.OptimizationJob;
import com.nazgul.optimization.domain.model.OptimizationJobStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OptimizationJobPersistenceAdapter implements OptimizationJobStore {

    private final OptimizationJobJpaRepository optimizationJobJpaRepository;
    private final OptimizationJobPersistenceMapper optimizationJobPersistenceMapper;

    @Override
    public OptimizationJob save(OptimizationJob job) {
        return optimizationJobPersistenceMapper.toDomain(
            optimizationJobJpaRepository.save(
                optimizationJobPersistenceMapper.toEntity(job)
            )
        );
    }

    @Override
    public Optional<OptimizationJob> findById(UUID jobId) {
        return optimizationJobJpaRepository
            .findById(jobId)
            .map(optimizationJobPersistenceMapper::toDomain);
    }

    @Override
    public List<OptimizationJob> findPendingJobs(int limit) {
        return optimizationJobJpaRepository
            .findByStatusOrderByCreatedAtAsc(
                OptimizationJobStatus.PENDING,
                PageRequest.of(0, limit)
            )
            .stream()
            .map(optimizationJobPersistenceMapper::toDomain)
            .toList();
    }
}
