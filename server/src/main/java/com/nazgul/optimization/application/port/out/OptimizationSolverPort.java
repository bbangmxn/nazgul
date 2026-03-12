package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.OptimizationJob;

public interface OptimizationSolverPort {

    String solve(OptimizationJob job, String planningOutput);
}
