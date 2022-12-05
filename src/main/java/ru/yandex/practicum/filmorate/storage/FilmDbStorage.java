package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Qualifier
@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT * FROM FILMS", this::makeFilms);
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "INSERT INTO FILMS (FILM_NAME, DESCRIPTION, DURATION, RATING_ID, RELEASE_DATE, GENRE_ID) VALUES (?,?,?,?,?,?)";
        if (film.getGenres() == null) {
            jdbcTemplate.update(sql,
                    film.getName(),
                    film.getDescription(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getReleaseDate(),
                    null);
        } else {
            for (Genres genre : film.getGenres()) {
                jdbcTemplate.update(sql,
                        film.getName(),
                        film.getDescription(),
                        film.getDuration(),
                        film.getMpa().getId(),
                        film.getReleaseDate(),
                        genre.getId());
            }
        }
        return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_NAME = ?", film.getName()));
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS set FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, RATING_ID = ?, RELEASE_DATE = ? where FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getReleaseDate(),
                film.getId());
        return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", film.getId()));
    }

    @Override
    public Film getFilm(int id) {
        return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id));
    }

    public void addLike(int userId, int filmId) {
        jdbcTemplate.update("MERGE INTO LIKES (USER_ID, FILM_ID) VALUES (?,?)", userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        String sql = "SELECT * FROM FILMS F " +
                "RIGHT OUTER JOIN (SELECT FILM_ID, COUNT(USER_ID) L FROM LIKES GROUP BY FILM_ID ORDER BY L DESC LIMIT ?) PF ON F.FILM_ID = PF.FILM_ID";
        return jdbcTemplate.query(sql, this::makeFilms, count);
    }

    private Film makeFilm(SqlRowSet rs) {
        if (rs.next()) {
            int id = rs.getInt("FILM_ID");
            int genreId = rs.getInt("GENRE_ID");
            log.info("2");
            String name = rs.getString("FILM_NAME");
            String description = rs.getString("DESCRIPTION");
            int duration = rs.getInt("DURATION");
            int ratingId = rs.getInt("RATING_ID");
            LocalDate releaseDate = Objects.requireNonNull(rs.getDate("RELEASE_DATE")).toLocalDate();
            List<Genres> genres = new ArrayList<>();
            genres.add(genreDbStorage.getGenre(genreId));
            return new Film(id, name, description, releaseDate, duration, genres, mpaDbStorage.getMpa(ratingId));
        }
        return null;
    }

    private Film makeFilms(ResultSet rs, int rowNum) {
        try {
            int id = rs.getInt("FILM_ID");
            int genreId = rs.getInt("GENRE_ID");
            String name = rs.getString("FILM_NAME");
            String description = rs.getString("DESCRIPTION");
            int duration = rs.getInt("DURATION");
            int ratingId = rs.getInt("RATING_ID");
            LocalDate releaseDate = Objects.requireNonNull(rs.getDate("RELEASE_DATE")).toLocalDate();
            return new Film(id, name, description, releaseDate, duration, Collections.singletonList(genreDbStorage.getGenre(genreId)), mpaDbStorage.getMpa(ratingId));
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
