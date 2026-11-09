package com.demo.token_bucket_rate_limiting.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Configuration
public class RateLimitConfiguration {
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String clientId) {
        return buckets.computeIfAbsent(clientId, id -> createNewBucket());
    }
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

}
