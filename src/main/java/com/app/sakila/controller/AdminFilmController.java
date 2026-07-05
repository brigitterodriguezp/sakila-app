package com.app.sakila.controller;

import com.app.sakila.dto.FilmDTO;
import com.app.sakila.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/films")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Películas (admin)", description = "Endpoints protegidos para administradores. Permiten crear, actualizar y eliminar " +
                                                "películas del catálogo del sistema.")
public class AdminFilmController {

    private final FilmService filmService;

    public AdminFilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva película",
               description = "Registra una nueva película en el catálogo. Requiere título, descripción, año de lanzamiento, " +
                             "duración, clasificación, idioma y lista de categorías y actores. " +
                             "Devuelve los detalles completos de la película creada con su ID asignado.")
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una película existente",
               description = "Modifica los datos de una película existente identificada por su ID. " +
                             "Se pueden actualizar título, descripción, año, duración, clasificación, " +
                             "idioma, categorías y actores. Devuelve los detalles actualizados de la película.")
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable Long id,
                                               @Valid @RequestBody FilmDTO dto) {
        return ResponseEntity.ok(filmService.updateFilm(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una película",
               description = "Elimina permanentemente una película del catálogo utilizando su ID. " +
                             "Si la película tiene alquileres asociados o copias en inventario, " +
                             "la operación puede fallar por restricciones de integridad referencial. " +
                             "Devuelve 204 sin contenido si se elimina correctamente.")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}
