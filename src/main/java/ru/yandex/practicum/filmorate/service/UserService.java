package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return userDbStorage.getUser(id);
    }

    public User updateUser(User user) {
        return userDbStorage.updateUser(user);
    }

    public User addUser(User user) {
        return userDbStorage.addUser(user);
    }

    public List<User> getAllFriends(int userId) {
        return userDbStorage.getAllFriends(userId);
    }

    public void addFriend(int userId, int friendId) {
        userDbStorage.addFriend(userId, friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userDbStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        return userDbStorage.getCommonFriends(user1Id, user2Id);
    }
}
