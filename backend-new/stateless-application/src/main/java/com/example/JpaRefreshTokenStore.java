package com.example;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.entity.RefreshToken;
import com.example.app.entity.UserCredentials;

import io.github.userauthstarter.autoconfigure.RefreshTokenRecord;
import io.github.userauthstarter.autoconfigure.RefreshTokenStore;

@Component
public class JpaRefreshTokenStore implements RefreshTokenStore {

    private final RefreshTokenRepository refreshRepository;
    private final UserCredentialsRepository credentialsRepository;

    public JpaRefreshTokenStore(RefreshTokenRepository refreshRepository, UserCredentialsRepository credentialsRepository) {
        this.refreshRepository = refreshRepository;
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    @Transactional
    public void save(String username, String tokenHash, Instant expiresAt) {
        UserCredentials credentials = credentialsRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        RefreshToken token = new RefreshToken(
                credentials.getUser(),
                tokenHash,
                OffsetDateTime.ofInstant(expiresAt, ZoneOffset.UTC));

        refreshRepository.save(token);
    }

    @Override
    @Transactional
    public Optional<RefreshTokenRecord> consume(String tokenHash, Instant now) {
        OffsetDateTime timestamp = OffsetDateTime.ofInstant(now, ZoneOffset.UTC);

        Optional<RefreshToken> token = refreshRepository.findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(tokenHash, timestamp);

        if (token.isEmpty()) {
            return Optional.empty();
        }

        Long userId = token.get().getUser().getId();

        if (refreshRepository.consumeIfActive(tokenHash, timestamp) != 1) {
            return Optional.empty();
        }

        String username = credentialsRepository.findByUser_Id(userId)
                .map(UserCredentials::getUsername)
                .orElseThrow(() -> new IllegalStateException("User credentials not found"));

        return Optional.of(new RefreshTokenRecord(username));
    }

    @Override
    @Transactional
    public void revoke(String tokenHash, Instant now) {
        refreshRepository.revokeIfNotRevoked(
                tokenHash,
                OffsetDateTime.ofInstant(now, ZoneOffset.UTC));
    }
}