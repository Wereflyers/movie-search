package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmService.getFilm(id);
    }

    @PostMapping
    public Film addFilm (@Valid @RequestBody Film film) {
        log.info("Got request POST /films");
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm (@Valid @RequestBody Film film) {
        log.info("Got request PUT /films");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike (@PathVariable int id, @PathVariable int userId) {
        log.info("User {} likes film {}", userId, id);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void removeLike (@PathVariable int id, @PathVariable int userId) {
        log.info("User {} removes like from film {}", userId, id);
        filmService.removeLike(userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms (@RequestParam(defaultValue = "10") int count) {
        log.info("GET {} most popular films", count);
        return filmService.getMostPopularFilms(count);
    }
}
