package com.demo.token_bucket_rate_limiting.configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class RateLimitConfiguration {
    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfiguration;

    public RateLimitConfiguration(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
        this.bucketConfiguration = BucketConfiguration.builder()
                        .addLimit(Bandwidth.classic(10,
                         io.github.bucket4j.Refill.greedy(10, Duration.ofMinutes(1))))
                        .build();
    }

    public Bucket resolveBucket(String clientId) {
        String redisKey = "rate-limit:v1:" + clientId;
        return proxyManager.getProxy(redisKey, () -> bucketConfiguration);
    }
}
