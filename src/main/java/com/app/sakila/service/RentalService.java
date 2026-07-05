package com.app.sakila.service;

import com.app.sakila.dto.RentalDTO;
import com.app.sakila.entity.Inventory;
import com.app.sakila.entity.Rental;
import com.app.sakila.exception.ResourceNotFoundException;
import com.app.sakila.mapper.RentalMapper;
import com.app.sakila.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final InventoryRepository inventoryRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final RentalMapper rentalMapper;

    public RentalService(RentalRepository rentalRepository,
                         InventoryRepository inventoryRepository,
                         CustomerRepository customerRepository,
                         StaffRepository staffRepository,
                         RentalMapper rentalMapper) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.rentalMapper = rentalMapper;
    }

    @Transactional(readOnly = true)
    public List<RentalDTO> getMyActiveRentals(Long customerId) {
        return rentalRepository.findByCustomerIdAndReturnDateIsNull(customerId).stream()
                .map(rentalMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RentalDTO> getMyRentalHistory(Long customerId) {
        return rentalRepository.findByCustomerIdOrderByRentalDateDesc(customerId).stream()
                .map(rentalMapper::toDTO)
                .toList();
    }

    @Transactional
    public RentalDTO rentFilm(Long customerId, Long filmId, Long staffId) {
        var customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", customerId));
        var staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff", staffId));

        var available = inventoryRepository.findAvailableByFilmId(filmId);
        if (available.isEmpty()) {
            throw new IllegalStateException("No available inventory for this film");
        }

        var inventory = available.get(0);
        var rental = new Rental();
        rental.setInventory(inventory);
        rental.setCustomer(customer);
        rental.setStaff(staff);
        rental = rentalRepository.save(rental);

        return rentalMapper.toDTO(rental);
    }

    @Transactional
    public RentalDTO returnFilm(Long rentalId) {
        var rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental", rentalId));

        if (rental.getReturnDate() != null) {
            throw new IllegalStateException("Film already returned");
        }

        rental.setReturnDate(LocalDateTime.now());
        rental = rentalRepository.save(rental);

        return rentalMapper.toDTO(rental);
    }
}
