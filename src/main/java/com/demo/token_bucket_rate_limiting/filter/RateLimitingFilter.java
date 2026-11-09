package com.demo.token_bucket_rate_limiting.filter;

import com.demo.token_bucket_rate_limiting.configuration.RateLimitConfiguration;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private final RateLimitConfiguration rateLimitConfig;

    public RateLimitingFilter(RateLimitConfiguration rateLimitConfig) {
        this.rateLimitConfig = rateLimitConfig;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        // Identify client with IP address
        String clientId = "IP:" + request.getRemoteAddr();
        // Get client's bucket
        Bucket bucket = rateLimitConfig.resolveBucket(clientId);
        // Try consuming one token
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (probe.isConsumed()) {
            response.setHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            long waitSeconds = Math.max(
                    1, (probe.getNanosToWaitForRefill() + 999_999_999L) / 1_000_000_000L);
            response.setStatus(429);
            response.setContentType("application/json");
            response.setHeader("Retry-After", String.valueOf(waitSeconds));
            response.getWriter().write("{\"error\":\"Too many requests. Please try again later.\"}");
        }
    }
}

