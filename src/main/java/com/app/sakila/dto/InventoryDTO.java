package com.app.sakila.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Inventory item representing one physical copy of a movie.")
public record InventoryDTO(
    @Schema(example = "1", description = "Existing inventory item identifier.")
    Long id,

    @Schema(example = "1", description = "Existing Sakila film identifier associated with the inventory item.")
    Long filmId,

    @Schema(example = "ACADEMY DINOSAUR", description = "Real movie title from the Sakila database.")
    String filmTitle,

    @Schema(example = "1", description = "Store identifier associated with the inventory item.")
    Integer storeId
) {}
