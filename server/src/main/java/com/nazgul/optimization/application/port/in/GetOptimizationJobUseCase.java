package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.OptimizationJob;

import java.util.UUID;

public interface GetOptimizationJobUseCase {

    OptimizationJob getJob(UUID jobId);
}
