package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend (@PathVariable int id, @PathVariable int friendId) {
        log.info("User {} got a new friend {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend (@PathVariable int id, @PathVariable int friendId) {
        log.info("User {} and {} are not friends anymore", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List <User> getAllFriends (@PathVariable int id) {
        log.info("Got friends of user {}", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends (@PathVariable int id, @PathVariable int otherId) {
        log.info("GET user {} and user {} common friends", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
