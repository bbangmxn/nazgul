package com.nazgul.optimization.application.port.out;

import com.nazgul.optimization.domain.model.OptimizationJob;

public interface OptimizationPlanningPort {

    String buildPlan(OptimizationJob job);
}
