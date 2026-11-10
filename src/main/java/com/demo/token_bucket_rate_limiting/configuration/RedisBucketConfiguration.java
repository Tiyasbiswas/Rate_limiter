package com.demo.token_bucket_rate_limiting.configuration;

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.Bucket4jLettuce;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RedisBucketConfiguration {
    @Bean //ProxyManager<String> ->distributed buckets using String keys
    public ProxyManager<String> proxyManager(
            StatefulRedisConnection<String, byte[]> redisConnection) {
        return Bucket4jLettuce
                .casBasedBuilder(redisConnection)
                .expirationAfterWrite(ExpirationAfterWriteStrategy
                .basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build();
    }
}
