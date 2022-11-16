package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Got request GET /users");
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUser (@PathVariable int userId) {
        log.info("Got request GET user by id = {}", userId);
        return userService.getUser(userId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Got request POST /users");
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Got request PUT /users");
        return userService.updateUser(user);
    }

    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addFriend (@PathVariable int userId, @PathVariable int friendId) {
        log.info("User {} got a new friend {}", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{userId}/friends/{friendId}")
    public void removeFriend (@PathVariable int userId, @PathVariable int friendId) {
        log.info("User {} and {} are not friends anymore", userId, friendId);
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/users/{userId}/friends")
    public List <User> getAllFriends (@PathVariable int userId) {
        log.info("Got friends of user {}", userId);
        return userService.getAllFriends(userId);
    }

    @GetMapping("/users/{user1Id}/common/{user2Id}")
    public List<User> getCommonFriends (@PathVariable int user1Id, @PathVariable int user2Id) {
        log.info("GET user {} and user {} common friends", user1Id, user2Id);
        return userService.getCommonFriends(user1Id, user2Id);
    }
}
