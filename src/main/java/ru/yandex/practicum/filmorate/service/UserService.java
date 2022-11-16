package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public List<User> getAllFriends(int userId) {
        List<User> friendsList = new ArrayList<>();
        userStorage.getUser(userId).getFriendsList()
                .forEach(i -> friendsList.add(userStorage.getUser(i)));
        return friendsList;
    }

    public void addFriend(int userId, int friendId) {
        User user1 = userStorage.getUser(userId);
        User user2 = userStorage.getUser(friendId);
        Set <Integer> friendsList1 = new HashSet<>();
        Set <Integer> friendsList2 = new HashSet<>();
        if(user1.getFriendsList() != null)
            friendsList1 = user1.getFriendsList();
        if (user2.getFriendsList() != null)
            friendsList2 = user2.getFriendsList();
        friendsList1.add(friendId);
        friendsList2.add(userId);
        user1.setFriendsList(friendsList1);
        user2.setFriendsList(friendsList2);
        userStorage.updateUser(user1);
        userStorage.updateUser(user2);
    }

    public void removeFriend(int userId, int friendId) {
        Set <Integer> friendsList = userStorage.getUser(userId).getFriendsList();
        if (!friendsList.contains(friendId)) {
            throw new NullPointerException("He is not your friend");
        }
        friendsList.remove(friendId);
        userStorage.getUser(userId).setFriendsList(friendsList);
        friendsList = userStorage.getUser(friendId).getFriendsList();
        if (!friendsList.contains(userId)) {
            throw new NullPointerException("He is not your friend");
        }
        friendsList.remove(userId);
        userStorage.getUser(friendId).setFriendsList(friendsList);
    }

    public List<User> getCommonFriends(int user1Id, int user2Id) {
        User user = userStorage.getUser(user1Id);
        User user2 = userStorage.getUser(user2Id);
        log.info("Not here");
        List<User> friends = new ArrayList<>();
        try {
            friends = user.getFriendsList().stream()
                    .filter(id -> user2.getFriendsList().contains(id))
                    .map(userStorage::getUser)
                    .collect(Collectors.toList());
            log.info("Not 2");
        } catch (NullPointerException ignored) {
            log.info("Here");
        }
        log.info("Not here");
        return friends;
    }
}
