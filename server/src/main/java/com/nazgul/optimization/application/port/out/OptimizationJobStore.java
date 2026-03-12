package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.OptimizationJob;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OptimizationJobStore {

    OptimizationJob save(OptimizationJob job);

    Optional<OptimizationJob> findById(UUID jobId);

    List<OptimizationJob> findPendingJobs(int limit);
}
