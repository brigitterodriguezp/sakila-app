package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body to add a new inventory item (physical copy of a film).")
public record AddInventoryRequest(
    @Schema(example = "1", description = "Existing Sakila film identifier to add inventory for.")
    @NotNull Long filmId,

    @Schema(example = "1", description = "Store identifier where the copy will be located.")
    Long storeId
) {}
