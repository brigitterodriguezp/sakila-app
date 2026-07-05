package com.app.sakila.controller;

import com.app.sakila.dto.CategoryDTO;
import com.app.sakila.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Categorías (admin)", description = "Endpoints protegidos para administradores. Permiten crear, actualizar y eliminar " +
                                                 "categorías de películas del sistema.")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva categoría",
               description = "Registra una nueva categoría de película en el sistema. " +
                             "Requiere un nombre descriptivo único. Devuelve los detalles completos " +
                             "de la categoría creada con su ID asignado.")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una categoría existente",
               description = "Modifica el nombre de una categoría existente identificada por su ID. " +
                             "El nuevo nombre debe ser único en el sistema. " +
                             "Devuelve los detalles actualizados de la categoría.")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id,
                                                       @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una categoría",
               description = "Elimina permanentemente una categoría del sistema utilizando su ID. " +
                             "Si existen películas asociadas a esta categoría, la operación puede fallar " +
                             "por restricciones de integridad referencial. " +
                             "Devuelve 204 sin contenido si se elimina correctamente.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
