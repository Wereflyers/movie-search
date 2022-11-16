package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int userId, int filmId) {
        Set <Integer> likes = filmStorage.getFilm(filmId).getLikes();
        likes.add(userId);
        filmStorage.getFilm(filmId).setLikes(likes);
    }

    public void removeLike(int userId, int filmId) {
        Set <Integer> likes = filmStorage.getFilm(filmId).getLikes();
        if (likes.isEmpty())
            throw new NullPointerException("Film has no likes.");
        likes.remove(userId);
        filmStorage.getFilm(filmId).setLikes(likes);
    }

    public List<Film> getMostPopularFilms(int count) {
        List <Film> popularFilms = new ArrayList<>();
        filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(Film::getId))
                .limit(count)
                .forEach(popularFilms::add);
        return popularFilms;
    }
}
