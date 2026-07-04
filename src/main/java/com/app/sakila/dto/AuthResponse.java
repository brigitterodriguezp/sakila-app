package com.app.sakila.dto;

public record AuthResponse(
    String token,
    String username,
    String role
) {}
