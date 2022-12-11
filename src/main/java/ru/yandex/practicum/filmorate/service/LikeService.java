package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeDbStorage likeDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;

    public void addLike(int userId, int filmId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User not found");
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        likeDbStorage.addLike(userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User not found");
        if (filmDbStorage.getFilm(filmId) == null)
            throw new NullPointerException("Film not found");
        likeDbStorage.removeLike(userId, filmId);
    }
}
