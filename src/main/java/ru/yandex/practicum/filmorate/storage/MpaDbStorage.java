package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getRatings() {
        return jdbcTemplate.query("SELECT * FROM RATING_MPA", this::makeMpa);
    }

    public Mpa getMpa(int id) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet("SELECT * FROM RATING_MPA WHERE RATING_ID = ?", id);
        rs.next();
        return new Mpa(rs.getInt("RATING_ID"), rs.getString("RATING_NAME"));
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) {
        try {
            int id = rs.getInt("RATING_ID");
            String name = rs.getString("RATING_NAME");
            return new Mpa(id, name);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
