package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static Map<Integer, User> users = new HashMap<>();
    private static int id = 1;

    public List<User> getUsers() {
       return new ArrayList<>(users.values());
    }

    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new NullPointerException("User doesn't exist");
        }
        return users.get(id);
    }

    public User addUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        user.setId(id);
        id = id + 1;
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId()))
            throw new NullPointerException("Cant find this user");
        validateUser(user);
        if (user.getName().isBlank())
            user.setName(user.getLogin());
        users.put(user.getId(), user);
        return user;
    }

    private void validateUser (User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("Email is incorrect");
        if (user.getLogin().isBlank() || user.getLogin() == null)
            throw new ValidationException("Login is incorrect");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Are you from the future?");
    }
}
