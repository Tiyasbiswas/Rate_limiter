package com.demo.token_bucket_rate_limiting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TokenBucketRateLimitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				TokenBucketRateLimitingApplication.class, args);
	}

}
