package io.github.userauthstarter.token;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Standard response returned by the built-in login endpoint. */
public record JwtTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn) {
}
