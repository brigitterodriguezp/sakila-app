package com.app.sakila.controller;

import com.app.sakila.dto.RentRequest;
import com.app.sakila.dto.RentalDTO;
import com.app.sakila.dto.ReturnRequest;
import com.app.sakila.security.JwtAuthenticationFilter.UserPrincipal;
import com.app.sakila.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Alquileres", description = "Endpoints protegidos para gestionar alquileres de películas del usuario autenticado. " +
                                        "Requiere token JWT en el encabezado 'Authorization'.")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping("/my-active-rentals")
    @Operation(summary = "Mis alquileres activos",
               description = "Devuelve todos los alquileres del usuario autenticado que aún no han sido devueltos " +
                             "(returnDate es null). Incluye ID del alquiler, título de la película, " +
                             "fecha de alquiler y estado ACTIVE.")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Alquileres activos del usuario.",
        content = @Content(examples = @ExampleObject(
            "[{\"id\": 16050, \"filmTitle\": \"ACADEMY DINOSAUR\", \"rentalDate\": \"2026-07-05T17:25:16\", \"returnDate\": null, \"status\": \"ACTIVE\"}]"
        ))))
    public ResponseEntity<List<RentalDTO>> getMyActiveRentals(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(rentalService.getMyActiveRentals(principal.userId()));
    }

    @GetMapping("/my-history")
    @Operation(summary = "Historial de alquileres",
               description = "Devuelve todos los alquileres del usuario autenticado, tanto activos como devueltos, " +
                             "ordenados del más reciente al más antiguo. Incluye ID del alquiler, título de la película, " +
                             "fechas de alquiler y devolución, y estado actual (ACTIVE o RETURNED).")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de alquileres del usuario.",
        content = @Content(examples = @ExampleObject(
            "[{\"id\": 16050, \"filmTitle\": \"ACADEMY DINOSAUR\", \"rentalDate\": \"2026-07-05T17:25:16\", \"returnDate\": null, \"status\": \"ACTIVE\"}, {\"id\": 15315, \"filmTitle\": \"BIKINI BORROWERS\", \"rentalDate\": \"2022-08-22T20:03:46\", \"returnDate\": \"2022-08-30T01:51:46\", \"status\": \"RETURNED\"}]"
        ))))
    public ResponseEntity<List<RentalDTO>> getMyHistory(
            @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(rentalService.getMyRentalHistory(principal.userId()));
    }

    @PostMapping("/rent")
    @Operation(summary = "Alquilar una película",
               description = "Crea un nuevo alquiler para el usuario autenticado. Requiere el ID de la película a alquilar. " +
                             "El parámetro 'staffId' es opcional; por defecto se asigna el empleado con ID 1. " +
                             "Si la película no tiene inventario disponible, la operación falla.")
    public ResponseEntity<RentalDTO> rentFilm(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody RentRequest request) {
        Long staffId = request.staffId() != null ? request.staffId() : 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rentalService.rentFilm(principal.userId(), request.filmId(), staffId));
    }

    @PutMapping("/return")
    @Operation(summary = "Devolver una película",
               description = "Registra la devolución de un alquiler existente. Requiere el ID del alquiler (rentalId) " +
                             "que se desea devolver. Actualiza la fecha de devolución con la fecha y hora actual del sistema.")
    public ResponseEntity<RentalDTO> returnFilm(@Valid @RequestBody ReturnRequest request) {
        return ResponseEntity.ok(rentalService.returnFilm(request.rentalId()));
    }
}
