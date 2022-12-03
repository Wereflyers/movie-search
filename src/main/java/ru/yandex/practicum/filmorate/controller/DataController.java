package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class DataController {
    private final FilmService filmService;

    @Autowired
    public DataController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/genres")
    public List<Genre> getGenre() {
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<RatingMpa> getMpas() {
        return filmService.getRatings();
    }

    @GetMapping("/mpa/{id}")
    public RatingMpa getMpa(@PathVariable int id) {
        return filmService.getMpa(id);
    }
}
