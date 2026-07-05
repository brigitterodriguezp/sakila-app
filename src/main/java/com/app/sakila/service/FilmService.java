package com.app.sakila.service;

import com.app.sakila.dto.CategoryDTO;
import com.app.sakila.dto.FilmDTO;
import com.app.sakila.entity.Film;
import com.app.sakila.exception.ResourceNotFoundException;
import com.app.sakila.mapper.FilmMapper;
import com.app.sakila.repository.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;

    public FilmService(FilmRepository filmRepository, FilmMapper filmMapper) {
        this.filmRepository = filmRepository;
        this.filmMapper = filmMapper;
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> getAllFilms() {
        return filmRepository.findAll().stream()
                .map(filmMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public FilmDTO getFilmById(Long id) {
        return filmRepository.findById(id)
                .map(filmMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Film", id));
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> getFilmCategories(Long filmId) {
        return filmRepository.findById(filmId)
                .map(film -> film.getCategories().stream()
                        .map(c -> new CategoryDTO(c.getId(), c.getName()))
                        .toList())
                .orElseThrow(() -> new ResourceNotFoundException("Film", filmId));
    }

    @Transactional(readOnly = true)
    public List<FilmDTO> searchFilms(String title) {
        return filmRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(filmMapper::toDTO)
                .toList();
    }

    @Transactional
    public FilmDTO createFilm(FilmDTO dto) {
        var film = filmMapper.toEntity(dto);
        film = filmRepository.save(film);
        return filmMapper.toDTO(film);
    }

    @Transactional
    public FilmDTO updateFilm(Long id, FilmDTO dto) {
        var film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film", id));
        filmMapper.updateEntity(dto, film);
        film = filmRepository.save(film);
        return filmMapper.toDTO(film);
    }

    @Transactional
    public void deleteFilm(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new ResourceNotFoundException("Film", id);
        }
        filmRepository.deleteById(id);
    }
}
