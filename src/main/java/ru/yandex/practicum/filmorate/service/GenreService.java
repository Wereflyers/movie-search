package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreService(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    public List<Genres> getGenres() {
        return genreDbStorage.getGenres();
    }

    public Genres getGenre(int id) {
        if (genreDbStorage.getGenre(id) == null)
            throw new NullPointerException("Id is wrong");
        return genreDbStorage.getGenre(id);
    }
}
