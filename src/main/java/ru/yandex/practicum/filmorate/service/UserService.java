package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserDbStorage userDbStorage;
    @Autowired
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    public User getUser(int id) {
        if (userDbStorage.getUser(id) == null)
            throw new NullPointerException("User id = " + id + " not found");
        return userDbStorage.getUser(id);
    }

    public User updateUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (userDbStorage.getUser(user.getId()) == null)
            throw new NullPointerException("User id = " + user.getId() + " not found");
        return userDbStorage.updateUser(user);
    }

    public User addUser(User user) {
        validateUser(user);
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        return userDbStorage.addUser(user);
    }

    public List<User> getAllFriends(int userId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User id = " + userId + " not found");
        return userDbStorage.getAllFriends(userId);
    }

    public void addFriend(int userId, int friendId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User id = " + userId + " not found");
        if (userDbStorage.getUser(friendId) == null)
            throw new NullPointerException("User id = " + friendId + " not found");
        userDbStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userDbStorage.getUser(userId) == null)
            throw new NullPointerException("User id = " + userId + " not found");
        if (userDbStorage.getUser(friendId) == null)
            throw new NullPointerException("User id = " + friendId + " not found");
        userDbStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        if (userDbStorage.getUser(user1Id) == null)
            throw new NullPointerException("User id = " + user1Id + " not found");
        if (userDbStorage.getUser(user2Id) == null)
            throw new NullPointerException("User id = " + user2Id + " not found");
        return userDbStorage.getCommonFriends(user1Id, user2Id);
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
