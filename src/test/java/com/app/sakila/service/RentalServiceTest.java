package com.app.sakila.service;

import com.app.sakila.dto.RentalDTO;
import com.app.sakila.entity.Rental;
import com.app.sakila.exception.ResourceNotFoundException;
import com.app.sakila.mapper.RentalMapper;
import com.app.sakila.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private RentalMapper rentalMapper;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void returnFilm_shouldSetReturnDateAndReturnDTO() {
        var rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(null);
        var now = LocalDateTime.now();

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(rental)).thenAnswer(invocation -> {
            var saved = invocation.<Rental>getArgument(0);
            saved.setReturnDate(now);
            return saved;
        });
        when(rentalMapper.toDTO(rental)).thenReturn(
                new RentalDTO(1L, "ACADEMY DINOSAUR", now.minusDays(2), now, "RETURNED")
        );

        var result = rentalService.returnFilm(1L);

        assertNotNull(result);
        assertEquals("RETURNED", result.status());
        assertNotNull(result.returnDate());
        verify(rentalRepository).findById(1L);
        verify(rentalRepository).save(rental);
        verify(rentalMapper).toDTO(rental);
    }

    @Test
    void returnFilm_shouldThrowWhenFilmAlreadyReturned() {
        var rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(LocalDateTime.now());

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(rental));

        var ex = assertThrows(IllegalStateException.class,
                () -> rentalService.returnFilm(1L));
        assertEquals("Film already returned", ex.getMessage());
        verify(rentalRepository).findById(1L);
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void returnFilm_shouldThrowWhenRentalNotFound() {
        when(rentalRepository.findById(999L)).thenReturn(Optional.empty());

        var ex = assertThrows(ResourceNotFoundException.class,
                () -> rentalService.returnFilm(999L));
        assertTrue(ex.getMessage().contains("Rental"));
        assertTrue(ex.getMessage().contains("999"));
        verify(rentalRepository).findById(999L);
        verify(rentalRepository, never()).save(any());
    }
}
