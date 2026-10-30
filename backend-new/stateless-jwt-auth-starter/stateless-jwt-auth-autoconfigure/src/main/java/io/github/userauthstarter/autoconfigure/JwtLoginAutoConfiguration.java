package io.github.userauthstarter.autoconfigure;

import io.github.userauthstarter.web.JwtAuthenticationController;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Adds username/password login and JWT issuance when the consuming application provides
 * a UserDetailsService. The repository and entity model stay owned by the application.
 */
@AutoConfiguration(
        after = JwtSecurityAutoConfiguration.class,
        beforeName = "org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration")
@ConditionalOnProperty(
        prefix = "app.jwt",
        name = {"enabled", "login-endpoint-enabled"},
        havingValue = "true",
        matchIfMissing = true)
@ConditionalOnBean(UserDetailsService.class)
public class JwtLoginAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationProvider.class)
    AuthenticationProvider jwtDaoAuthenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    AuthenticationManager jwtAuthenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationController.class)
    JwtAuthenticationController jwtAuthenticationController(
            AuthenticationManager authenticationManager,
            io.github.userauthstarter.token.JwtTokenService jwtTokenService) {
        return new JwtAuthenticationController(authenticationManager, jwtTokenService);
    }
}