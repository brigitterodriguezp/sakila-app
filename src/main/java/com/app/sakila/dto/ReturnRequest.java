package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to return a rented film.")
public record ReturnRequest(
    @Schema(example = "11496", description = "Existing rental identifier to close. Must be an active rental (returnDate is null). Use /api/rentals/my-active-rentals to find active rentals.")
    @NotNull Long rentalId
) {}
