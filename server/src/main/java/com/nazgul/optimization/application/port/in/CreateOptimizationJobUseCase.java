package com.nazgul.optimization.application.port.in;

import com.nazgul.optimization.domain.model.OptimizationJob;

public interface CreateOptimizationJobUseCase {

    OptimizationJob createJob(CreateOptimizationJobCommand command);
}
