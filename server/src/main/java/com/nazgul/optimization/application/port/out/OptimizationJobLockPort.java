package com.nazgul.optimization.application.port.out;

import java.time.Duration;

public interface OptimizationJobLockPort {

    boolean tryLock(String key, Duration ttl);

    void unlock(String key);
}
