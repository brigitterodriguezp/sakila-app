package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response returned after a successful login.")
public record AuthResponse(
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.example.token", description = "JWT token used to authenticate protected requests.")
    String token,

    @Schema(example = "admin", description = "Authenticated username.")
    String username,

    @Schema(example = "ADMIN", description = "Role assigned to the authenticated user.")
    String role
) {}
