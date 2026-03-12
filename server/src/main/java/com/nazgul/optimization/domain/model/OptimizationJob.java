package com.nazgul.optimization.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationJob {

    private UUID id;
    private Long requestedByUserId;
    private String title;
    private String objective;
    private String inputSummary;
    private String inputPayload;
    private boolean aiAnalysisEnabled;
    private OptimizationJobStatus status;
    private String aiAnalysis;
    private String solutionSummary;
    private String failureReason;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OptimizationJob pending(
            Long requestedByUserId,
            String title,
            String objective,
            String inputSummary,
            String inputPayload,
            boolean aiAnalysisEnabled
    ) {
        LocalDateTime now = LocalDateTime.now();
        return OptimizationJob.builder()
                .id(UUID.randomUUID())
                .requestedByUserId(requestedByUserId)
                .title(title)
                .objective(objective)
                .inputSummary(inputSummary)
                .inputPayload(inputPayload)
                .aiAnalysisEnabled(aiAnalysisEnabled)
                .status(OptimizationJobStatus.PENDING)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void markProcessing() {
        this.status = OptimizationJobStatus.PROCESSING;
        this.startedAt = LocalDateTime.now();
        this.updatedAt = this.startedAt;
    }

    public void complete(String aiAnalysis, String solutionSummary) {
        LocalDateTime now = LocalDateTime.now();
        this.status = OptimizationJobStatus.COMPLETED;
        this.aiAnalysis = aiAnalysis;
        this.solutionSummary = solutionSummary;
        this.failureReason = null;
        this.completedAt = now;
        this.updatedAt = now;
    }

    public void fail(String failureReason) {
        LocalDateTime now = LocalDateTime.now();
        this.status = OptimizationJobStatus.FAILED;
        this.failureReason = failureReason;
        this.completedAt = now;
        this.updatedAt = now;
    }
}
