package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static Map <Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.info("Got request GET /films");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm (@Valid @RequestBody Film film) {
        log.info("Got request POST /films");
        if (film.getId() == 0)
            film.setId(1);
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm (@Valid @RequestBody Film film) {
        log.info("Got request PUT /films");
        if (!films.containsKey(film.getId()))
            throw new ValidationException("Films doesn't exist");
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validateFilm (Film film) {
        if (film.getName().isBlank())
            throw new ValidationException("The name is incorrect");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Too long description");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Films dont exist that time");
        if (film.getDuration() <=0)
            throw new ValidationException("The duration of film is incorrect");
    }
}
