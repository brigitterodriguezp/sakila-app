package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Film data returned by the API or used for movie management.")
public record FilmDTO(
    @Schema(example = "1", description = "Existing Sakila film identifier.")
    Long id,

    @Schema(example = "ACADEMY DINOSAUR", description = "Real movie title from the Sakila database.")
    @NotBlank String title,

    @Schema(example = "A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies", description = "Real Sakila movie description.")
    String description,

    @Schema(example = "2006", description = "Release year stored in Sakila.")
    Integer releaseYear,

    @Schema(example = "PG", description = "Movie rating stored in Sakila.")
    String rating,

    @Schema(example = "0.99", description = "Rental rate stored in Sakila.")
    @NotNull BigDecimal rentalRate,

    @Schema(example = "86", description = "Movie duration in minutes.")
    Short length,

    @Schema(example = "English", description = "Movie language stored in Sakila.")
    String language
) {}
