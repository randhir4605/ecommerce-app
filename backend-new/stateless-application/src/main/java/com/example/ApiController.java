package com.example;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @GetMapping("/api/public/status")
    public Map<String, String> publicStatus() {

        return Map.of(
                "status", "Application is running"
        );
    }

    @GetMapping("/api/secure/me")
    public Map<String, Object> currentUser(
            Authentication authentication) {

        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList()
        );
    }

    @GetMapping("/api/secure/ping")
    public Map<String, String> securePing() {

        return Map.of(
                "message", "JWT authentication successful"
        );
    }
}