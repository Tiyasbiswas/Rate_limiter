package com.demo.token_bucket_rate_limiting.configuration;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RadisConfiguration {
// --------Create the Redis connection-------
    //1.Creates and manages the Redis client
@Bean(destroyMethod = "shutdown")
public RedisClient redisClient(
        @Value("${app.rate-limit.redis-uri}") String redisUri) {

    return RedisClient.create(redisUri);
}


    //2.Represents the connection to Redis
    @Bean(destroyMethod = "close")
    public StatefulRedisConnection<String, byte[]> redisConnection(
            RedisClient redisClient) {

        return redisClient.connect(
                RedisCodec.of(
                        StringCodec.UTF8,
                        ByteArrayCodec.INSTANCE
                )
        );
    }
}
