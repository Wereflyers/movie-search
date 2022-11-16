package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return inMemoryFilmStorage.getFilms();
    }

    @PostMapping
    public Film addFilm (@Valid @RequestBody Film film) {
        log.info("Got request POST /films");
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm (@Valid @RequestBody Film film) {
        log.info("Got request PUT /films");
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike (@PathVariable int filmId, @PathVariable int userId) {
        log.info("User {} likes film {}", userId, filmId);
        filmService.addLike(userId, filmId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike (@PathVariable int filmId, @PathVariable int userId) {
        log.info("User {} removes like from film {}", userId, filmId);
        filmService.removeLike(userId, filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms (@RequestParam(defaultValue = "10") int count) {
        log.info("GET {} most popular films", count);
        return filmService.getMostPopularFilms(count);
    }
}
