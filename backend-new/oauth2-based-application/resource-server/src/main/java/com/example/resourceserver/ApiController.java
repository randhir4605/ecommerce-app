package com.example.resourceserver;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/public/status")
    public Map<String, String> publicStatus() {
        return Map.of("message", "Resource Server is running");
    }

    @GetMapping("/api/orders")
    public Map<String, Object> getOrders(Authentication authentication) {
        return Map.of("message", "Orders retrieved successfully", "username", authentication.getName());
    }
}