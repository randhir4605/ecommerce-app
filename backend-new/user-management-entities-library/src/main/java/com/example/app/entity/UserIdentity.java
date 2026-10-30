package com.example.app.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "user_identity",
    uniqueConstraints = @UniqueConstraint(name = "uq_user_identity_provider_subject",
        columnNames = {"provider", "provider_subject"}),
    indexes = @Index(name = "idx_user_identity_user_id", columnList = "user_id"))
public class UserIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IdentityProvider provider;

    @Column(name = "provider_subject", nullable = false, length = 255)
    private String providerSubject;
    @Column(name = "provider_email", length = 254)
    private String providerEmail;
    @Column(name = "provider_email_verified")
    private Boolean providerEmailVerified;
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    protected UserIdentity() {}
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public IdentityProvider getProvider() { return provider; }
    public void setProvider(IdentityProvider provider) { this.provider = provider; }
    public String getProviderSubject() { return providerSubject; }
    public void setProviderSubject(String providerSubject) { this.providerSubject = providerSubject; }
    public String getProviderEmail() { return providerEmail; }
    public void setProviderEmail(String providerEmail) { this.providerEmail = providerEmail; }
    public Boolean getProviderEmailVerified() { return providerEmailVerified; }
    public void setProviderEmailVerified(Boolean providerEmailVerified) { this.providerEmailVerified = providerEmailVerified; }
    public OffsetDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(OffsetDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
