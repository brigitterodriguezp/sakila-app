package com.app.sakila.controller;

import com.app.sakila.dto.CategoryDTO;
import com.app.sakila.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categorías (público)", description = "Endpoints públicos para consultar las categorías de películas disponibles en el sistema.")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las categorías",
               description = "Obtiene el listado completo de categorías de películas disponibles en el sistema, " +
                             "como Acción, Comedia, Drama, Ciencia Ficción, etc. Cada categoría incluye " +
                             "su identificador único y su nombre descriptivo.")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
