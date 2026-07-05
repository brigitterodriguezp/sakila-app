package com.app.sakila.controller;

import com.app.sakila.dto.FilmDTO;
import com.app.sakila.service.FilmService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/films")
@PreAuthorize("hasRole('ADMIN')")
public class AdminFilmController {

    private final FilmService filmService;

    public AdminFilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<FilmDTO> createFilm(@Valid @RequestBody FilmDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(filmService.createFilm(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable Long id,
                                               @Valid @RequestBody FilmDTO dto) {
        return ResponseEntity.ok(filmService.updateFilm(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent().build();
    }
}
