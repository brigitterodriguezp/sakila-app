package com.app.sakila.controller;

import com.app.sakila.dto.CategoryDTO;
import com.app.sakila.dto.FilmDTO;
import com.app.sakila.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@Tag(name = "Películas (público)", description = "Endpoints públicos para consultar el catálogo de películas disponibles en el sistema.")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las películas",
               description = "Obtiene el listado completo de películas registradas en el sistema, " +
                             "incluyendo título, descripción, año de lanzamiento, duración, clasificación, " +
                             "idioma original y categorías asociadas.")
    public ResponseEntity<List<FilmDTO>> getAllFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}/categories")
    @Operation(summary = "Categorías de una película",
               description = "Obtiene el listado de categorías asociadas a una película específica. " +
                             "Cada película puede pertenecer a una o varias categorías como Action, Comedy, Drama, etc.")
    public ResponseEntity<List<CategoryDTO>> getFilmCategories(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilmCategories(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una película por ID",
               description = "Busca y devuelve los detalles completos de una película específica " +
                             "utilizando su identificador único (ID). Incluye título, descripción, " +
                             "año, duración, clasificación, idioma, categorías y actores.")
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar películas por título",
               description = "Busca películas cuyo título contenga total o parcialmente el texto proporcionado. " +
                             "La búsqueda no distingue mayúsculas/minúsculas y devuelve todas las coincidencias " +
                             "con sus detalles completos.")
    public ResponseEntity<List<FilmDTO>> searchFilms(@RequestParam String title) {
        return ResponseEntity.ok(filmService.searchFilms(title));
    }
}
