package com.example.backend.config;

import com.example.backend.service.GoogleOAuth2UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

private final GoogleOAuth2UserService googleOAuth2UserService;

@Value("${app.frontend-url}")
private String frontendUrl;

public SecurityConfig(
        GoogleOAuth2UserService googleOAuth2UserService) {
            System.out.println("raj "+googleOAuth2UserService);
    this.googleOAuth2UserService = googleOAuth2UserService;
}

@Bean
public SecurityFilterChain securityFilterChain(
        HttpSecurity http) throws Exception {

    RequestMatcher apiRequest = request ->
            request.getRequestURI().startsWith("/api/");

    http
        .cors(Customizer.withDefaults())

        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/",
                "/error",
                "/backend_test",
                "/db_test",
                "/api/products/**",
                "/products/**",
                "/api/search/**",
                "/search/**",
                "/api/cart/**",
                "/cart/**",
                "/oauth2/**",
                "/login/**"
            ).permitAll()

            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

            .anyRequest().authenticated()
        )

        .exceptionHandling(ex -> ex
            .defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                apiRequest
            )
        )

        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(userInfo -> userInfo
                .userService(googleOAuth2UserService)
            )
            .defaultSuccessUrl(
                frontendUrl + "/?login=success",
                true
            )
        );

    return http.build();
}

@Bean
public CorsConfigurationSource corsConfigurationSource() {

    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of(frontendUrl));
    configuration.setAllowedMethods(List.of(
        "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
    ));
    configuration.setAllowedHeaders(List.of(
        "Authorization",
        "Content-Type",
        "X-XSRF-TOKEN"
    ));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
}


}
