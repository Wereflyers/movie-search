package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.List;

@Service
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
        return filmDbStorage.addFilm(film);
    }

    public Film updateFilm (Film film) {
        return filmDbStorage.updateFilm(film);
    }

    public void addLike(int userId, int filmId) {
        filmDbStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        filmDbStorage.removeLike(userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmDbStorage.getMostPopularFilms(count);
    }

    public List<Genre> getGenres() {
        return filmDbStorage.getGenres();
    }

    public Genre getGenre(int id) {
        return filmDbStorage.getGenre(id);
    }

    public RatingMpa getMpa(int id) {
        return filmDbStorage.getMpa(id);
    }

    public List<RatingMpa> getRatings() {
        return filmDbStorage.getRatings();
    }
}
