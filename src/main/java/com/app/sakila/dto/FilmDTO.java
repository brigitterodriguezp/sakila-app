package com.app.sakila.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record FilmDTO(
    Long id,
    @NotBlank String title,
    String description,
    Integer releaseYear,
    String rating,
    @NotNull BigDecimal rentalRate,
    Short length,
    String language
) {}
