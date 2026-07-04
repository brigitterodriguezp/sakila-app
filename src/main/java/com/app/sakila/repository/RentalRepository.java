package com.app.sakila.repository;

import com.app.sakila.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByCustomerIdAndReturnDateIsNull(Long customerId);

    List<Rental> findByCustomerIdOrderByRentalDateDesc(Long customerId);

    long countByInventoryIdAndReturnDateIsNull(Long inventoryId);
}
