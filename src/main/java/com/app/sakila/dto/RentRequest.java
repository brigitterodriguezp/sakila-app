package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to rent a film.")
public record RentRequest(
    @Schema(example = "1", description = "Existing Sakila film identifier to rent.")
    @NotNull Long filmId,

    @Schema(example = "1", description = "Existing Sakila staff identifier processing the rental.")
    Long staffId
) {}
