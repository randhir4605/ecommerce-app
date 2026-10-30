package io.github.userauthstarter.token;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import io.github.userauthstarter.autoconfigure.JwtSecurityProperties;
import io.github.userauthstarter.autoconfigure.RefreshTokenRecord;
import io.github.userauthstarter.autoconfigure.RefreshTokenStore;
import java.util.Collection;
import org.springframework.security.core.userdetails.UserDetailsService;

/** Creates signed HS256 access tokens for successfully authenticated principals. */
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtSecurityProperties properties;
    private final RefreshTokenStore refreshTokenStore;
    private final UserDetailsService userDetailsService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final Clock clock;

    public JwtTokenService(JwtEncoder jwtEncoder,JwtSecurityProperties properties,
    		RefreshTokenStore refreshTokenStore,UserDetailsService userDetailsService, Clock clock) {
    	this.jwtEncoder = jwtEncoder;
        this.properties = properties;
        this.refreshTokenStore = refreshTokenStore;
        this.userDetailsService = userDetailsService;
        this.clock = clock;

        Duration ttl = properties.getAccessTokenTtl();
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            throw new IllegalStateException("app.jwt.access-token-ttl must be a positive duration.");
        }
        if (properties.getIssuer() == null || properties.getIssuer().isBlank()) {
            throw new IllegalStateException("app.jwt.issuer must not be blank.");
        }
    }
    
    public JwtTokenResponse createAccessToken(Authentication authentication) {
        return createTokens(authentication.getName(), authentication.getAuthorities());
    }

    public JwtTokenResponse createAccessToken(UserDetails userDetails) {
        return createTokens(userDetails.getUsername(), userDetails.getAuthorities());
    }

    private JwtTokenResponse createTokens(String username, Collection<? extends GrantedAuthority> authorities) {
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.getAccessTokenTtl());
        Instant refreshExpiry = issuedAt.plus(properties.getRefreshTokenTtl());

        List<String> authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .distinct()
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .subject(username)
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("authorities", authorityNames)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        String refreshToken = generateRefreshToken();
        refreshTokenStore.save(username, hash(refreshToken), refreshExpiry);

        return new JwtTokenResponse(accessToken, "Bearer", properties.getAccessTokenTtl().toSeconds(), refreshToken, properties.getRefreshTokenTtl().toSeconds());
    }
    
    public JwtTokenResponse refresh(String rawRefreshToken) {
        RefreshTokenRecord record = refreshTokenStore.consume(hash(rawRefreshToken), clock.instant())
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(record.username());

        if (!userDetails.isEnabled() || !userDetails.isAccountNonLocked() || !userDetails.isAccountNonExpired() || !userDetails.isCredentialsNonExpired()) {
            throw new BadCredentialsException("User account is not available");
        }

        return createAccessToken(userDetails);
    }

    public void logout(String rawRefreshToken) {
        refreshTokenStore.revoke(hash(rawRefreshToken), Instant.now());
    }

    private String generateRefreshToken() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to hash refresh token", exception);
        }
    }
}

