package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Category data used to create, update, and return movie categories.")
public record CategoryDTO(
    @Schema(example = "1", description = "Existing Sakila category identifier.")
    Long id,

    @Schema(example = "Action", description = "Real category name from the Sakila database.")
    @NotBlank String name
) {}
