package com.nazgul.optimization.application.service;

import com.nazgul.optimization.application.port.in.CreateOptimizationJobCommand;
import com.nazgul.optimization.application.port.in.CreateOptimizationJobUseCase;
import com.nazgul.optimization.application.port.in.GetOptimizationJobUseCase;
import com.nazgul.optimization.application.port.in.ProcessOptimizationJobsUseCase;
import com.nazgul.optimization.application.port.out.OptimizationJobLockPort;
import com.nazgul.optimization.application.port.out.OptimizationJobStore;
import com.nazgul.optimization.application.port.out.OptimizationPlanningPort;
import com.nazgul.optimization.application.port.out.OptimizationSolverPort;
import com.nazgul.optimization.config.OptimizationProperties;
import com.nazgul.optimization.domain.model.OptimizationJob;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptimizationApplicationService implements
        CreateOptimizationJobUseCase,
        GetOptimizationJobUseCase,
        ProcessOptimizationJobsUseCase {

    private final OptimizationJobStore optimizationJobStore;
    private final OptimizationPlanningPort optimizationPlanningPort;
    private final OptimizationSolverPort optimizationSolverPort;
    private final OptimizationJobLockPort optimizationJobLockPort;
    private final OptimizationProperties optimizationProperties;

    @Override
    @Transactional
    public OptimizationJob createJob(CreateOptimizationJobCommand command) {
        OptimizationJob job = OptimizationJob.pending(
                command.requestedByUserId(),
                command.title(),
                command.objective(),
                command.inputSummary(),
                command.inputPayload(),
                command.aiAnalysisEnabled()
        );

        return optimizationJobStore.save(job);
    }

    @Override
    @Transactional(readOnly = true)
    public OptimizationJob getJob(UUID jobId) {
        return optimizationJobStore.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("최적화 잡을 찾을 수 없습니다."));
    }

    @Override
    public void processPendingJobs() {
        List<OptimizationJob> pendingJobs = optimizationJobStore.findPendingJobs(optimizationProperties.batchSize());

        for (OptimizationJob pendingJob : pendingJobs) {
            String lockKey = "optimization:job:" + pendingJob.getId();
            boolean locked = optimizationJobLockPort.tryLock(
                    lockKey,
                    Duration.ofSeconds(optimizationProperties.lockTtlSeconds())
            );

            if (!locked) {
                continue;
            }

            try {
                processSingleJob(pendingJob.getId());
            } finally {
                optimizationJobLockPort.unlock(lockKey);
            }
        }
    }

    private void processSingleJob(UUID jobId) {
        OptimizationJob job = getJob(jobId);

        try {
            job.markProcessing();
            optimizationJobStore.save(job);

            String planningOutput = job.isAiAnalysisEnabled()
                    ? optimizationPlanningPort.buildPlan(job)
                    : "AI 분석이 비활성화되어 기본 계획 생성을 건너뛰었습니다.";

            String solutionSummary = optimizationSolverPort.solve(job, planningOutput);

            job.complete(planningOutput, solutionSummary);
            optimizationJobStore.save(job);
        } catch (IllegalArgumentException e) {
            log.debug("Skip optimization job {}: {}", jobId, e.getMessage());
        } catch (Exception e) {
            log.error("Optimization job {} failed", jobId, e);
            job.fail(e.getMessage());
            optimizationJobStore.save(job);
        }
    }
}
