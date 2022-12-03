package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static Map<Integer, Film> films = new HashMap<>();
    private static int id = 1;

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm (Film film) {
        film.setId(id);
        validateFilm(film);
        films.put(film.getId(), film);
        id = id + 1;
        return film;
    }

    public Film updateFilm (Film film) {
        if (!films.containsKey(film.getId()))
            throw new NullPointerException("Films doesn't exist");
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new NullPointerException("Film doesn't exist");
        }
        return films.get(id);
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
