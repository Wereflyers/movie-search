package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genres> getGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES", this::makeGenre);
    }

    public Genres getGenre(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE GENRE_ID = ?", id);
        if (rs.next()) {
            return new Genres(rs.getInt("GENRE_ID"), rs.getString("GENRE"));
        }
        return null;
    }

    public void addFilmGenres(List<Genres> genres, int filmId) {
       for (Genres genre : genres) {
                jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ( ?,? )", filmId, genre.getId());
       }
    }

    public List<Genres> getFilmGenres(int filmId) {
        return jdbcTemplate.query("SELECT * FROM GENRES " +
                "JOIN (SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?) AS I ON GENRES.GENRE_ID = I.GENRE_ID", this::makeGenre, filmId);
    }

    public void removeFilmGenres(int filmId) {
        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", filmId);
    }

    private Genres makeGenre(ResultSet rs, int rowNum) {
        try {
            int id = rs.getInt("GENRE_ID");
            String name = rs.getString("GENRE");
            return new Genres(id, name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
