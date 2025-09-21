package com.snapp.billsplitter.infrastructure.service.bucket4j;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final Map<Long, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(Long userId) {
        return cache.computeIfAbsent(userId, id -> {
            Refill refill = Refill.greedy(2, Duration.ofMinutes(10));
            Bandwidth limit = Bandwidth.classic(2, refill);
            return Bucket.builder().addLimit(limit).build();
        });
    }
}
