package io.github.userauthstarter.autoconfigure;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import io.github.userauthstarter.token.JwtTokenService;
import io.github.userauthstarter.web.JwtAuthenticationController;

@AutoConfiguration(beforeName = {
        "org.springframework.boot.security.oauth2.server.resource.autoconfigure.OAuth2ResourceServerAutoConfiguration",
        "org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration"
})
@ConditionalOnClass({HttpSecurity.class, JwtDecoder.class, NimbusJwtEncoder.class})
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnProperty(prefix = "app.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(JwtSecurityProperties.class)
public class JwtSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(SecretKey.class)
    SecretKey jwtSecretKey(JwtSecurityProperties properties) {
        String configuredSecret = properties.getSecretBase64();
        if (configuredSecret == null || configuredSecret.isBlank()) {
            throw new IllegalStateException(
                    "Missing app.jwt.secret-base64. Configure a strong Base64-encoded secret " +
                    "(at least 32 random bytes after Base64 decoding).");
        }

        final byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(configuredSecret);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException(
                    "app.jwt.secret-base64 must be valid standard Base64.", exception);
        }

        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "app.jwt.secret-base64 must decode to at least 32 random bytes for HS256.");
        }

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    @Bean
    @ConditionalOnMissingBean(JwtEncoder.class)
    JwtEncoder jwtEncoder(SecretKey secretKey) {
        return NimbusJwtEncoder.withSecretKey(secretKey)
                .algorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(JwtDecoder.class)
    JwtDecoder jwtDecoder(SecretKey secretKey, JwtSecurityProperties properties) {
        if (properties.getIssuer() == null || properties.getIssuer().isBlank()) {
            throw new IllegalStateException("app.jwt.issuer must not be blank.");
        }

        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(properties.getIssuer()));
        return decoder;
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    PasswordEncoder jwtPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    Clock jwtClock() {
        return Clock.systemUTC();
    }

    @Bean
    @ConditionalOnBean(UserDetailsService.class)
    @ConditionalOnMissingBean
    JwtTokenService jwtTokenService(JwtEncoder jwtEncoder, JwtSecurityProperties properties, 
    		RefreshTokenStore refreshTokenStore, UserDetailsService userDetailsService, Clock clock) {
        return new JwtTokenService(jwtEncoder, properties, refreshTokenStore, userDetailsService, clock);
    }
    
    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationController.class)
    @ConditionalOnProperty(prefix = "app.jwt", name = "login-endpoint-enabled", havingValue = "true", matchIfMissing = true)
    JwtAuthenticationController jwtAuthenticationController(AuthenticationManager manager, JwtTokenService tokenService) {
        return new JwtAuthenticationController(manager, tokenService);
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationConverter.class)
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();
        Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter = jwt -> {
            Set<GrantedAuthority> authorities = new LinkedHashSet<>();

            Collection<GrantedAuthority> scopeAuthorities = scopeConverter.convert(jwt);
            if (scopeAuthorities != null) {
                authorities.addAll(scopeAuthorities);
            }

            Object claim = jwt.getClaims().get("authorities");
            if (claim instanceof Collection<?> values) {
                for (Object value : values) {
                    if (value instanceof String authority && !authority.isBlank()) {
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }
                }
            }
            return new ArrayList<>(authorities);
        };

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    /**
     * Provides stateless bearer-token security unless the application supplies its own
     * SecurityFilterChain. All routes require a valid JWT except configured public paths.
     */
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http, JwtSecurityProperties properties,
            JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {

        Set<String> permittedPaths = new LinkedHashSet<>(properties.getPublicPaths());
        if (properties.isLoginEndpointEnabled()
                && properties.getLoginPath() != null
                && !properties.getLoginPath().isBlank()) {
            permittedPaths.add(properties.getLoginPath());
        }

        http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> {
                List<String> allowedPaths = new ArrayList<>(properties.getPublicPaths());

                if (properties.isLoginEndpointEnabled()) {
                    allowedPaths.add(properties.getLoginPath());
                    allowedPaths.add(properties.getRefreshPath());
                    allowedPaths.add(properties.getLogoutPath());
                }

                auth.requestMatchers(allowedPaths.toArray(String[]::new)).permitAll();
                auth.anyRequest().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));

        return http.build();
    }
}
