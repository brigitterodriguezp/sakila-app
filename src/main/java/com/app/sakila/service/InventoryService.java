package com.app.sakila.service;

import com.app.sakila.dto.InventoryDTO;
import com.app.sakila.entity.Inventory;
import com.app.sakila.exception.ResourceNotFoundException;
import com.app.sakila.repository.FilmRepository;
import com.app.sakila.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                            FilmRepository filmRepository) {
        this.inventoryRepository = inventoryRepository;
        this.filmRepository = filmRepository;
    }

    public List<InventoryDTO> getInventoryByFilm(Long filmId) {
        return inventoryRepository.findByFilmId(filmId).stream()
                .map(i -> new InventoryDTO(i.getId(), i.getFilm().getId(),
                        i.getFilm().getTitle(), i.getStoreId()))
                .toList();
    }

    @Transactional
    public InventoryDTO addInventory(Long filmId, Long storeId) {
        var film = filmRepository.findById(filmId)
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));

        var inventory = new Inventory();
        inventory.setFilm(film);
        inventory.setStoreId(storeId.intValue());
        inventory = inventoryRepository.save(inventory);

        return new InventoryDTO(inventory.getId(), film.getId(), film.getTitle(), inventory.getStoreId());
    }

    @Transactional
    public void removeInventory(Long inventoryId) {
        if (!inventoryRepository.existsById(inventoryId)) {
            throw new ResourceNotFoundException("Inventory", inventoryId);
        }
        inventoryRepository.deleteById(inventoryId);
    }
}
