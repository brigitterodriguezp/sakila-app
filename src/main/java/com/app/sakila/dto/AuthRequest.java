package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request used to authenticate an existing user.")
public record AuthRequest(
    @Schema(example = "admin", description = "Existing username registered in the system.")
    @NotBlank String username,

    @Schema(example = "Admin12345", description = "Plain password sent only in the request. It must never be stored as plain text.")
    @NotBlank String password
) {}
