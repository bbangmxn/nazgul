package com.nazgul.optimization.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.optimization")
public record OptimizationProperties(
        boolean workerEnabled,
        long workerDelayMs,
        long lockTtlSeconds,
        int batchSize,
        String promptTemplate
) {
}
