package com.demo.token_bucket_rate_limiting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {
    @GetMapping("/api/items")
    public String getAllItems() {
        return "Showing all items!";
    }
}
