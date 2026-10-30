package com.example;

import com.example.app.entity.RefreshToken;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHashAndRevokedAtIsNullAndExpiresAtAfter(String tokenHash, OffsetDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE RefreshToken t SET t.revokedAt = :now WHERE t.tokenHash = :hash AND t.revokedAt IS NULL AND t.expiresAt > :now")
    int consumeIfActive(@Param("hash") String hash, @Param("now") OffsetDateTime now);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revokedAt = :now WHERE t.tokenHash = :hash AND t.revokedAt IS NULL")
    int revokeIfNotRevoked(@Param("hash") String hash, @Param("now") OffsetDateTime now);
}