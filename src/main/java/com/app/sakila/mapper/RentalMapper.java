package com.app.sakila.mapper;

import com.app.sakila.dto.RentalDTO;
import com.app.sakila.entity.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public RentalDTO toDTO(Rental rental) {
        return new RentalDTO(
            rental.getId(),
            rental.getInventory().getFilm().getTitle(),
            rental.getRentalDate(),
            rental.getReturnDate(),
            rental.getReturnDate() != null ? "RETURNED" : "ACTIVE"
        );
    }
}
