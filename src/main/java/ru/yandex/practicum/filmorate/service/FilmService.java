package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id);
    }

    public Film addFilm (Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm (Film film) {
        return filmStorage.updateFilm(film);
    }

    public void addLike(int userId, int filmId) {
        Film film = filmStorage.getFilm(filmId);
        Set <Integer> likes = new HashSet<>();
        if (film.getLikes() != null)
            likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
        filmStorage.updateFilm(film);
    }

    public void removeLike(int userId, int filmId) {
        Set<Integer> likes = filmStorage.getFilm(filmId).getLikes();
        if (likes.isEmpty())
            throw new NullPointerException("Film has no likes.");
        likes.remove(userId);
        filmStorage.getFilm(filmId).setLikes(likes);
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>();
        filmStorage.getFilms().stream()
                .filter(f -> f.getLikes() != null)
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .forEach(popularFilms::add);
        if (popularFilms.size() < count) {
            int limit = count - popularFilms.size();
            filmStorage.getFilms().stream()
                    .filter(f -> f.getLikes() == null)
                    .limit(limit)
                    .forEach(popularFilms::add);
        }
        return popularFilms;
    }
}
