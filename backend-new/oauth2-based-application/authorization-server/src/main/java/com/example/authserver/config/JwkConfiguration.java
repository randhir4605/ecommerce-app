package com.example.authserver.config;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import com.example.authserver.components.ResourceIndicatorPolicy;
import com.example.authserver.service.OidcUserInfoService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration(proxyBeanMethods = false)
public class JwkConfiguration {

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair keyPair = generator.generateKeyPair();

            RSAKey rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey((RSAPrivateKey) keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();

            return new ImmutableJWKSet<>(new JWKSet(rsaKey));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to generate RSA signing key", exception);
        }
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }
    
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer(OidcUserInfoService oidcUserInfoService,
    		ResourceIndicatorPolicy resourcePolicy) {

        return context -> {
        	
        	// Customize access token
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())
                    && context.getAuthorizationGrant() instanceof OAuth2AuthorizationCodeAuthenticationToken grant) {

                String audience = resourcePolicy.resolveAudience(
                    context.getRegisteredClient().getClientId(),
                    grant.getAdditionalParameters().get("resource")
                );
                context.getClaims().audience(List.of(audience));
            }
            
           // Customize ID token
            if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
        	    OidcUserInfo userInfo = oidcUserInfoService.loadUser(context.getPrincipal().getName(),context.getAuthorizedScopes());
        	    context.getClaims().claims(claims -> claims.putAll(userInfo.getClaims()));
        	}
        };
    }
}

