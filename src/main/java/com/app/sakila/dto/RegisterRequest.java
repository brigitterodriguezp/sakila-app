package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request used to register a new system user.")
public record RegisterRequest(
    @Schema(example = "user1", description = "Unique username for the new account.")
    @NotBlank @Size(min = 3, max = 50) String username,

    @Schema(example = "user1@example.com", description = "Valid email address for the new account.")
    @NotBlank @Email @Size(max = 100) String email,

    @Schema(example = "User12345", description = "Plain password sent only during registration. It must be stored using BCrypt.")
    @NotBlank @Size(min = 6, max = 100) String password
) {}
