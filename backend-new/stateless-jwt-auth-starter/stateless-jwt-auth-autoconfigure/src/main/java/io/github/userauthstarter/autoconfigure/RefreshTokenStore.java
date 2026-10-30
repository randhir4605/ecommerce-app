package io.github.userauthstarter.autoconfigure;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenStore {
    void save(String username, String tokenHash, Instant expiresAt);
    Optional<RefreshTokenRecord> consume(String tokenHash, Instant now);
    void revoke(String tokenHash, Instant now);
}