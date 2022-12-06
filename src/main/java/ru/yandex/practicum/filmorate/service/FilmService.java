package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<Film> getFilms() {
        return filmDbStorage.getFilms();
    }

    public Film getFilm(int id) {
        if (filmDbStorage.getFilm(id) == null) {
            throw new NullPointerException("Film not found");
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
            throw new NullPointerException("Film not found");
        if(film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Too early for films");
        return filmDbStorage.updateFilm(film);
    }

    public void addLike(int userId, int filmId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User not found");
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        filmDbStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User not found");
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        filmDbStorage.removeLike(userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return filmDbStorage.getMostPopularFilms(count);
    }
}
