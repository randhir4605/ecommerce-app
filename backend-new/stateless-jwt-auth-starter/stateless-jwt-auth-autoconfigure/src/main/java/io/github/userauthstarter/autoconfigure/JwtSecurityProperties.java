package io.github.userauthstarter.autoconfigure;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the stateless JWT authentication starter.
 *
 * Values are normally supplied under the {@code app.jwt} prefix.
 */
@ConfigurationProperties(prefix = "app.jwt")
public class JwtSecurityProperties {

    /** Base64-encoded HMAC key. The decoded key must contain at least 32 bytes. */
    private String secretBase64;

    /** Value written into the JWT {@code iss} claim and validated on incoming tokens. */
    private String issuer = "user-management-auth";

    /** Lifetime of tokens created by the built-in login endpoint. */
    private Duration accessTokenTtl = Duration.ofMinutes(15);
    private Duration refreshTokenTtl = Duration.ofDays(30);

    /** Enable the starter's POST login endpoint when a UserDetailsService is present. */
    private boolean loginEndpointEnabled = true;

    /** Full path for the optional login endpoint. */
    private String loginPath = "/api/auth/login";
    private String refreshPath = "/api/auth/refresh";
    private String logoutPath = "/api/auth/logout";
    /** Routes that do not require a bearer token, in addition to the login path. */
    private List<String> publicPaths = new ArrayList<>(List.of("/api/public/**"));

    public String getSecretBase64() {
        return secretBase64;
    }

    public void setSecretBase64(String secretBase64) {
        this.secretBase64 = secretBase64;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Duration getAccessTokenTtl() {
        return accessTokenTtl;
    }

    public void setAccessTokenTtl(Duration accessTokenTtl) {
        this.accessTokenTtl = accessTokenTtl;
    }

    public Duration getRefreshTokenTtl() {
		return refreshTokenTtl;
	}

	public void setRefreshTokenTtl(Duration refreshTokenTtl) {
		this.refreshTokenTtl = refreshTokenTtl;
	}

	public boolean isLoginEndpointEnabled() {
        return loginEndpointEnabled;
    }

    public void setLoginEndpointEnabled(boolean loginEndpointEnabled) {
        this.loginEndpointEnabled = loginEndpointEnabled;
    }

    public String getLoginPath() {
        return loginPath;
    }

    public void setLoginPath(String loginPath) {
        this.loginPath = loginPath;
    }

    public String getRefreshPath() {
		return refreshPath;
	}

	public void setRefreshPath(String refreshPath) {
		this.refreshPath = refreshPath;
	}

	public List<String> getPublicPaths() {
        return publicPaths;
    }

    public String getLogoutPath() {
		return logoutPath;
	}

	public void setLogoutPath(String logoutPath) {
		this.logoutPath = logoutPath;
	}

	public void setPublicPaths(List<String> publicPaths) {
        this.publicPaths = publicPaths == null
                ? new ArrayList<>()
                : new ArrayList<>(publicPaths);
    }
}
