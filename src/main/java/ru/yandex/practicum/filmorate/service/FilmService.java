package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film getFilm(int id) {
        return filmDbStorage.getFilm(id);
    }

    public Film addFilm (Film film) {
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Too early for films");
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm (Film film) {
        if (filmDbStorage.getFilm(film.getId()) == null)
            throw new NullPointerException("Film not found");
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Too early for films");
        return filmDbStorage.updateFilm(film);
    }

    public void addLike(int userId, int filmId) {
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        filmDbStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        filmDbStorage.removeLike(userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmDbStorage.getMostPopularFilms(count);
    }
}
