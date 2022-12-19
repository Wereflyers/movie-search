package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film getFilm(int id) {
        if (filmDbStorage.getFilm(id) == null) {
            throw new NullPointerException("Film id = " + id + " not found");
        }
        return filmDbStorage.getFilm(id);
    }

    public Film addFilm (Film film) {
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Too early for films");
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm (Film film) {
        if (filmDbStorage.getFilm(film.getId()) == null)
            throw new NullPointerException("Film id = " + film.getId() + " not found");
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Too early for films");
        return filmDbStorage.updateFilm(film);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmDbStorage.getMostPopularFilms(count);
    }
}
