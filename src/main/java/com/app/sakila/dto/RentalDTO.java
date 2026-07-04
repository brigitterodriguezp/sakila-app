package com.app.sakila.dto;

import java.time.LocalDateTime;

public record RentalDTO(
    Long id,
    String filmTitle,
    LocalDateTime rentalDate,
    LocalDateTime returnDate,
    String status
) {}
