package io.github.userauthstarter.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.github.userauthstarter.autoconfigure.LogoutRequest;
import io.github.userauthstarter.autoconfigure.RefreshTokenRequest;
import io.github.userauthstarter.token.JwtTokenResponse;
import io.github.userauthstarter.token.JwtTokenService;
import jakarta.validation.Valid;

/** Optional login endpoint automatically registered when a UserDetailsService is present. */
@RestController
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public JwtAuthenticationController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("${app.jwt.login-path:/api/auth/login}")
    public JwtTokenResponse login(@Valid @RequestBody LoginRequest request) {
        Authentication authenticated;
        try {
            authenticated = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            request.username(), request.password()));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, exception.getMessage());
        }
        return jwtTokenService.createAccessToken(authenticated);
    }
    
    @PostMapping("${app.jwt.refresh-path:/api/auth/refresh}")
    public JwtTokenResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            return jwtTokenService.refresh(request.refreshToken());
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }
    }

    @PostMapping("${app.jwt.logout-path:/api/auth/logout}")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        jwtTokenService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
