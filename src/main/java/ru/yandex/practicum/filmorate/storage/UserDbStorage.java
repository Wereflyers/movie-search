package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Qualifier
@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query("SELECT * FROM FILMORATE_USER", this::makeUsers);
    }

    @Override
    public User addUser(User user) {
        jdbcTemplate.update("INSERT INTO FILMORATE_USER (USER_NAME, LOGIN, EMAIL, BIRTHDAY) " +
                        "VALUES ( ?,?,?,? )",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
        String sql = "SELECT * FROM FILMORATE_USER WHERE LOGIN = ?";
        return makeUser(jdbcTemplate.queryForRowSet(sql, user.getLogin()));
    }

    @Override
    public User updateUser(User user) {
        int result = jdbcTemplate.update("UPDATE FILMORATE_USER SET USER_NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY = ? WHERE USER_ID = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        if (result == 0)
            throw new NullPointerException("User not found");
        return makeUser(jdbcTemplate.queryForRowSet("SELECT * FROM FILMORATE_USER WHERE USER_ID = ?", user.getId()));
    }

    @Override
    public User getUser(int id) {
        User user = makeUser( jdbcTemplate.queryForRowSet("SELECT * FROM FILMORATE_USER WHERE USER_ID = ?", id));
        if (user == null) {
                throw new NullPointerException("User not found");
        }
        return user;
    }

    public List<User> getAllFriends(int userId) {
        String sql = "SELECT * FROM FILMORATE_USER " +
                "RIGHT OUTER JOIN (SELECT FRIEND_ID FROM FRIENDS_LIST WHERE USER_ID = ?) FR on FILMORATE_USER.USER_ID = FR.FRIEND_ID";
        return jdbcTemplate.query(sql, this::makeUsers, userId);
    }

    public void addFriend(int userId, int friendId) {
        List<Map<String, Object>> friendship = jdbcTemplate.queryForList("SELECT FRIEND_ID FROM FRIENDS_LIST WHERE USER_ID = ? AND FRIEND_ID = ?",
                friendId, userId);
        if (friendship.isEmpty()) {
            jdbcTemplate.update("INSERT INTO FRIENDS_LIST (USER_ID, FRIEND_ID) VALUES ( ?, ? )",
                    userId, friendId);
        } else {
            jdbcTemplate.update("INSERT INTO FRIENDS_LIST (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) VALUES ( ?, ?, ? )",
                    userId, friendId, true);
            jdbcTemplate.update("UPDATE FRIENDS_LIST SET FRIENDSHIP_STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?",
                    true, friendId, userId);
        }
    }

    public void removeFriend(int userId, int friendId) {
        List<Map<String, Object>> friendship = jdbcTemplate.queryForList("SELECT FRIEND_ID FROM FRIENDS_LIST WHERE USER_ID = ? AND FRIEND_ID = ?",
                friendId, userId);
        jdbcTemplate.update("DELETE FROM FRIENDS_LIST WHERE USER_ID = ? AND FRIEND_ID = ?", userId, friendId);
        if (!friendship.isEmpty()) {
            jdbcTemplate.update("UPDATE FRIENDS_LIST SET FRIENDSHIP_STATUS = ? WHERE USER_ID = ? AND FRIEND_ID = ?",
                    false, friendId, userId);
        }
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        String sql = "SELECT * FROM FILMORATE_USER " +
                "RIGHT OUTER JOIN (SELECT FRIEND_ID, COUNT(FRIEND_ID) FROM (SELECT USER_ID, FRIEND_ID FROM FRIENDS_LIST WHERE USER_ID = ? OR USER_ID = ?)" +
                "GROUP BY FRIEND_ID HAVING COUNT(FRIEND_ID) = 2) FR ON FR.FRIEND_ID = FILMORATE_USER.USER_ID";
        return jdbcTemplate.query(sql, this::makeUsers, user1Id, user2Id);
    }

    private User makeUser(SqlRowSet rs) {
        if (rs.next()) {
            int id = rs.getInt("USER_ID");
            String name = rs.getString("USER_NAME");
            String login = rs.getString("LOGIN");
            String email = rs.getString("EMAIL");
            LocalDate birthday = Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate();
            return new User(id, email, login, name, birthday);
        }
        return null;
    }

    private User makeUsers(ResultSet rs, int rowNum) {
        try {
                int id = rs.getInt("USER_ID");
                String name = rs.getString("USER_NAME");
                String login = rs.getString("LOGIN");
                String email = rs.getString("EMAIL");
                LocalDate birthday = Objects.requireNonNull(rs.getDate("BIRTHDAY")).toLocalDate();
                return new User(id, email, login, name, birthday);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
