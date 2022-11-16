package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Set <Integer> friendsList = userStorage.getUser(userId).getFriendsList();
        friendsList.add(friendId);
        userStorage.getUser(userId).setFriendsList(friendsList);
        friendsList = userStorage.getUser(friendId).getFriendsList();
        friendsList.add(userId);
        userStorage.getUser(friendId).setFriendsList(friendsList);
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
        List<User> mutualFriends = new ArrayList<>();
        List<Integer> friendsList1 = new ArrayList<>(userStorage.getUser(user1Id).getFriendsList());
        List<Integer> friendsList2 = new ArrayList<>(userStorage.getUser(user2Id).getFriendsList());
        if (friendsList1.isEmpty() || friendsList2.isEmpty())
            return mutualFriends;
        for (int id: friendsList1) {
            if (friendsList2.contains(id))
                mutualFriends.add(userStorage.getUser(id));
        }
        return mutualFriends;
    }
}
