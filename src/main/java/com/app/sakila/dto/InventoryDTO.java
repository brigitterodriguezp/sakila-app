package com.app.sakila.dto;

public record InventoryDTO(
    Long id,
    Long filmId,
    String filmTitle,
    Integer storeId
) {}
