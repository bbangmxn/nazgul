package com.nazgul.optimization.adapter.in.scheduler;

import com.nazgul.optimization.application.port.in.ProcessOptimizationJobsUseCase;
import com.nazgul.optimization.config.OptimizationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimizationJobWorker {

    private final ProcessOptimizationJobsUseCase processOptimizationJobsUseCase;
    private final OptimizationProperties optimizationProperties;

    @Scheduled(fixedDelayString = "${app.optimization.worker-delay-ms:30000}")
    public void run() {
        if (!optimizationProperties.workerEnabled()) {
            return;
        }

        log.debug("Polling optimization jobs");
        processOptimizationJobsUseCase.processPendingJobs();
    }
}
