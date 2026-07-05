package com.app.sakila.controller;

import com.app.sakila.dto.AddInventoryRequest;
import com.app.sakila.dto.InventoryDTO;
import com.app.sakila.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Inventario (admin)", description = "Endpoints protegidos para administradores. Permiten gestionar el inventario " +
                                                 "de copias físicas de películas disponibles en las tiendas.")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    public AdminInventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    @Operation(summary = "Consultar inventario de una película",
               description = "Obtiene todas las copias disponibles en inventario para una película específica. " +
                             "Requiere el ID de la película como parámetro. Devuelve el listado de copias " +
                             "con su ID de inventario, tienda y estado de disponibilidad.")
    public ResponseEntity<List<InventoryDTO>> getInventory(@RequestParam Long filmId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFilm(filmId));
    }

    @PostMapping
    @Operation(summary = "Agregar copia al inventario",
               description = "Añade una nueva copia de una película al inventario de una tienda. " +
                             "Requiere el ID de la película. El parámetro 'storeId' es opcional; " +
                             "por defecto se asigna la tienda con ID 1. Devuelve los detalles del nuevo registro de inventario.")
    public ResponseEntity<InventoryDTO> addInventory(@Valid @RequestBody AddInventoryRequest request) {
        Long storeId = request.storeId() != null ? request.storeId() : 1L;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.addInventory(request.filmId(), storeId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar copia del inventario",
               description = "Elimina permanentemente una copia del inventario utilizando su ID. " +
                             "Si la copia está actualmente alquilada, la operación puede fallar " +
                             "debido a restricciones de integridad referencial. Devuelve 204 sin contenido si se elimina correctamente.")
    public ResponseEntity<Void> removeInventory(@PathVariable Long id) {
        inventoryService.removeInventory(id);
        return ResponseEntity.noContent().build();
    }
}
