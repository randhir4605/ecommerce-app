package com.example.authserver.components;

import java.util.Map;
import java.util.Set;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.stereotype.Component;

@Component
public class ResourceIndicatorPolicy {

    private static final Map<String, ResourceDefinition> RESOURCES = Map.of(
        "http://localhost:8080/",
        new ResourceDefinition("cstore-orders-api", Set.of("cstore-postman")),

        "http://localhost:8081/",
        new ResourceDefinition("cstore-payments-api", Set.of("cstore-postman"))
    );

    public String resolveAudience(String clientId, Object resourceParameter) {
        if (!(resourceParameter instanceof String resource)) {
            throw invalidTarget();
        }

        ResourceDefinition definition = RESOURCES.get(resource);

        if (definition == null || !definition.allowedClients().contains(clientId)) {
            throw invalidTarget();
        }

        return definition.audience();
    }

    private OAuth2AuthenticationException invalidTarget() {
        return new OAuth2AuthenticationException(
            new OAuth2Error("invalid_target", "Requested resource is not allowed.", null)
        );
    }

    private record ResourceDefinition(String audience, Set<String> allowedClients) {}
}