package com.app.sakila.controller;

import com.app.sakila.dto.RentalDTO;
import com.app.sakila.security.JwtAuthenticationFilter.UserPrincipal;
import com.app.sakila.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/my-active-rentals")
    public ResponseEntity<List<RentalDTO>> getMyActiveRentals(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(rentalService.getMyActiveRentals(principal.userId()));
    }

    @GetMapping("/my-history")
    public ResponseEntity<List<RentalDTO>> getMyHistory(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(rentalService.getMyRentalHistory(principal.userId()));
    }

    @PostMapping("/rent")
    public ResponseEntity<RentalDTO> rentFilm(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestBody Map<String, Long> body) {
        Long filmId = body.get("filmId");
        Long staffId = body.getOrDefault("staffId", 1L);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentalService.rentFilm(principal.userId(), filmId, staffId));
    }

    @PutMapping("/return")
    public ResponseEntity<RentalDTO> returnFilm(@RequestBody Map<String, Long> body) {
        return ResponseEntity.ok(rentalService.returnFilm(body.get("rentalId")));
    }
}
