package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Qualifier
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
         SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS");
         try {
         while (rs.next()) {
             films.add(makeFilm(rs));
             rs.next();
         }
         films.add(makeFilm(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
         return films;
    }

    @Override
    public Film addFilm(Film film) {
        jdbcTemplate.update("INSERT INTO FILMS (GENRE_ID, FILM_NAME, DESCRIPTION, DURATION, RATING_ID, RELEASE_DATE) VALUES ( ?,?,?,?,?,?)",
                film.getGenreId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getRatingId(),
                film.getReleaseDate());
        try {
            return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", film.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update("UPDATE FILMS set GENRE_ID = ?, FILM_NAME = ?, DESCRIPTION = ?, DURATION = ?, RATING_ID = ?, RELEASE_DATE = ? where FILM_ID = ?",
                film.getGenreId(),
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getRatingId(),
                film.getReleaseDate(),
                film.getId());
        try {
            return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", film.getId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Film getFilm(int id) {
        try {
            return makeFilm(jdbcTemplate.queryForRowSet("SELECT * FROM FILMS WHERE FILM_ID = ?", id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLike(int userId, int filmId) {
        jdbcTemplate.update("MERGE INTO LIKES (USER_ID, FILM_ID) VALUES (?,?)", userId, filmId);
    }

    public void removeLike(int userId, int filmId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, filmId);
    }

    public List<Film> getMostPopularFilms(int count) {
        List<Film> popularFilms = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS F " +
                "RIGHT OUTER JOIN (SELECT FILM_ID, COUNT(USER_ID) L FROM LIKES GROUP BY FILM_ID ORDER BY L DESC LIMIT ?) PF ON F.FILM_ID = PF.FILM_ID",
                count);
        try {
            while (rs.next()) {
                popularFilms.add(makeFilm(rs));
                rs.next();
            }
            popularFilms.add(makeFilm(rs));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return popularFilms;
    }

    public List<Genre> getGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_GENRE");
        while (rs.next()) {
            genres.add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE")));
            rs.next();
        }
        genres.add(new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE")));
        return genres;
    }

    public Genre getGenre(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM FILM_GENRE WHERE GENRE_ID = ?", id);
        return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE"));
    }

    public List<RatingMpa> getRatings() {
        List<RatingMpa> ratings = new ArrayList<>();
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM RATING_MPA");
        while (rs.next()) {
            ratings.add(new RatingMpa(rs.getInt("RATING_ID"), rs.getString("RATING")));
            rs.next();
        }
        ratings.add(new RatingMpa(rs.getInt("RATING_ID"), rs.getString("RATING")));
        return ratings;
    }

    public RatingMpa getMpa(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM RATING_MPA WHERE RATING_ID = ?", id);
        return new RatingMpa(rs.getInt("RATING_ID"), rs.getString("RATING"));
    }

    private Film makeFilm(SqlRowSet rs) throws SQLException {
        if (rs.next()){
            int id = rs.getInt("FILM_ID");
            int genreId = rs.getInt("GENRE_ID");
            String name = rs.getString("FILM_NAME");
            String description = rs.getString("DESCRIPTION");
            int duration = rs.getInt("DURATION");
            int ratingId = rs.getInt("RATING_ID");
            LocalDate releaseDate = Objects.requireNonNull(rs.getDate("RELEASE_DATE")).toLocalDate();
            return new Film(id, name, description, releaseDate, duration, genreId, ratingId);
        }
        return null;
    }
}
