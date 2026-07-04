package com.app.sakila.repository;

import com.app.sakila.entity.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByFilmId(Long filmId);

    long countByFilmId(Long filmId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.id = :id")
    Optional<Inventory> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT i FROM Inventory i WHERE i.film.id = :filmId AND i.id NOT IN " +
           "(SELECT r.inventory.id FROM Rental r WHERE r.returnDate IS NULL)")
    List<Inventory> findAvailableByFilmId(@Param("filmId") Long filmId);
}
