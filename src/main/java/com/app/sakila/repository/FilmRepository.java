package com.app.sakila.repository;

import com.app.sakila.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByTitleContainingIgnoreCase(String title);
    List<Film> findByLanguageId(Integer languageId);
}
