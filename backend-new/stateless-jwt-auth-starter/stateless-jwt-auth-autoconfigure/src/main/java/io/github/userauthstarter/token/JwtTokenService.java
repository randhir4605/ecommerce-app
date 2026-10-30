package io.github.userauthstarter.token;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import io.github.userauthstarter.autoconfigure.JwtSecurityProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;

/** Creates signed HS256 access tokens for successfully authenticated principals. */
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtSecurityProperties properties;
    private final Clock clock;

    public JwtTokenService(
            JwtEncoder jwtEncoder,
            JwtSecurityProperties properties,
            Clock clock) {
        this.jwtEncoder = jwtEncoder;
        this.properties = properties;
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
        Instant issuedAt = clock.instant();
        Instant expiresAt = issuedAt.plus(properties.getAccessTokenTtl());
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .distinct()
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .subject(authentication.getName())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .claim("authorities", authorities)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).type("JWT").build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        long expiresIn = Duration.between(issuedAt, expiresAt).toSeconds();
        return new JwtTokenResponse(token, "Bearer", expiresIn);
    }
}
