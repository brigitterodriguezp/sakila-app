package com.app.sakila.controller;

import com.app.sakila.dto.InventoryDTO;
import com.app.sakila.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/inventory")
@PreAuthorize("hasRole('ADMIN')")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    public AdminInventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getInventory(@RequestParam Long filmId) {
        return ResponseEntity.ok(inventoryService.getInventoryByFilm(filmId));
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> addInventory(@RequestBody Map<String, Long> body) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventoryService.addInventory(body.get("filmId"), body.get("storeId")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeInventory(@PathVariable Long id) {
        inventoryService.removeInventory(id);
        return ResponseEntity.noContent().build();
    }
}
