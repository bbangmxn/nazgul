package com.nazgul.optimization.adapter.out.lock;

import com.nazgul.optimization.application.port.out.OptimizationJobLockPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisOptimizationJobLockAdapter implements OptimizationJobLockPort {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean tryLock(String key, Duration ttl) {
        try {
            return Boolean.TRUE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, "locked", ttl));
        } catch (Exception e) {
            log.warn("Redis lock unavailable for {}", key, e);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis unlock failed for {}", key, e);
        }
    }
}
