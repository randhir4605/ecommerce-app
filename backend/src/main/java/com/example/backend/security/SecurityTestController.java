package com.example.backend.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecurityTestController {

    @GetMapping("/api/public")
    public Map<String, String> publicEndpoint() {
        return Map.of("message", "Anyone can access this");
    }

    @GetMapping("/api/me")
    public Map<String, String> currentUser() {
        return Map.of("message", "You are authenticated");
    }
}
