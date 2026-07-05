package com.app.sakila.controller;

import com.app.sakila.dto.FilmDTO;
import com.app.sakila.service.FilmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ResponseEntity<List<FilmDTO>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FilmDTO>> searchFilms(@RequestParam String title) {
        return ResponseEntity.ok(filmService.searchFilms(title));
    }
}
