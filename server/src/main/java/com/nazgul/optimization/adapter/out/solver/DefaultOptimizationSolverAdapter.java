package com.nazgul.optimization.adapter.out.solver;

import com.nazgul.optimization.application.port.out.OptimizationSolverPort;
import com.nazgul.optimization.domain.model.OptimizationJob;
import org.springframework.stereotype.Component;

@Component
public class DefaultOptimizationSolverAdapter implements OptimizationSolverPort {

    @Override
    public String solve(OptimizationJob job, String planningOutput) {
        String normalizedPlan = planningOutput == null ? "" : planningOutput.trim();
        if (normalizedPlan.length() > 1200) {
            normalizedPlan = normalizedPlan.substring(0, 1200) + "...";
        }

        return """
                Optimization pipeline is ready.
                Objective: %s
                Mode: baseline placeholder solver
                Next: replace this adapter with a dedicated solver implementation behind OptimizationSolverPort.

                Planning summary:
                %s
                """.formatted(job.getObjective(), normalizedPlan);
    }
}
