package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private static Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getUsers() {
        log.info("Got request GET /users");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Got request POST /users");
        validateUser(user);
        if (user.getName().isBlank() || user.getName() == null)
            user.setName(user.getLogin());
        if (user.getId() == null)
            user.setId(users.size()+1);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Got request PUT /users");
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Cant find this user");
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
