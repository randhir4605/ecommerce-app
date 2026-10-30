package io.github.userauthstarter.web;

import jakarta.validation.constraints.NotBlank;

/** Request body for the built-in username/password login endpoint. */
public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password) {
}
