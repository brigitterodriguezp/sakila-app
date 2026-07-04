package com.app.sakila.mapper;

import com.app.sakila.dto.FilmDTO;
import com.app.sakila.entity.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public FilmDTO toDTO(Film film) {
        return new FilmDTO(
            film.getId(),
            film.getTitle(),
            film.getDescription(),
            film.getReleaseYear(),
            film.getRating(),
            film.getRentalRate(),
            film.getLength(),
            film.getLanguage() != null ? film.getLanguage().getName() : null
        );
    }

    public Film toEntity(FilmDTO dto) {
        var film = new Film();
        film.setTitle(dto.title());
        film.setDescription(dto.description());
        film.setReleaseYear(dto.releaseYear());
        film.setRating(dto.rating());
        film.setRentalRate(dto.rentalRate());
        film.setLength(dto.length());
        return film;
    }

    public void updateEntity(FilmDTO dto, Film film) {
        if (dto.title() != null) film.setTitle(dto.title());
        if (dto.description() != null) film.setDescription(dto.description());
        if (dto.releaseYear() != null) film.setReleaseYear(dto.releaseYear());
        if (dto.rating() != null) film.setRating(dto.rating());
        if (dto.rentalRate() != null) film.setRentalRate(dto.rentalRate());
        if (dto.length() != null) film.setLength(dto.length());
    }
}
