package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa getMpa(int id) {
        if (id < 1 || id > 5)
            throw new NullPointerException("Id is wrong");
        return mpaDbStorage.getMpa(id);
    }

    public List<Mpa> getRatings() {
        return mpaDbStorage.getRatings();
    }
}
