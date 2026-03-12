package com.nazgul.optimization.application.port.in;

public record CreateOptimizationJobCommand(
        Long requestedByUserId,
        String title,
        String objective,
        String inputSummary,
        String inputPayload,
        boolean aiAnalysisEnabled
) {
}
