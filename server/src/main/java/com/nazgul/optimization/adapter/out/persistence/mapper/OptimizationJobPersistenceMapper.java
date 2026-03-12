package com.nazgul.optimization.adapter.out.persistence.mapper;

import com.nazgul.optimization.adapter.out.persistence.entity.OptimizationJobEntity;
import com.nazgul.optimization.domain.model.OptimizationJob;
import org.springframework.stereotype.Component;

@Component
public class OptimizationJobPersistenceMapper {

    public OptimizationJob toDomain(OptimizationJobEntity entity) {
        return OptimizationJob.builder()
                .id(entity.getId())
                .requestedByUserId(entity.getRequestedByUserId())
                .title(entity.getTitle())
                .objective(entity.getObjective())
                .inputSummary(entity.getInputSummary())
                .inputPayload(entity.getInputPayload())
                .aiAnalysisEnabled(entity.isAiAnalysisEnabled())
                .status(entity.getStatus())
                .aiAnalysis(entity.getAiAnalysis())
                .solutionSummary(entity.getSolutionSummary())
                .failureReason(entity.getFailureReason())
                .startedAt(entity.getStartedAt())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public OptimizationJobEntity toEntity(OptimizationJob job) {
        return OptimizationJobEntity.builder()
                .id(job.getId())
                .requestedByUserId(job.getRequestedByUserId())
                .title(job.getTitle())
                .objective(job.getObjective())
                .inputSummary(job.getInputSummary())
                .inputPayload(job.getInputPayload())
                .aiAnalysisEnabled(job.isAiAnalysisEnabled())
                .status(job.getStatus())
                .aiAnalysis(job.getAiAnalysis())
                .solutionSummary(job.getSolutionSummary())
                .failureReason(job.getFailureReason())
                .startedAt(job.getStartedAt())
                .completedAt(job.getCompletedAt())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
    }
}
