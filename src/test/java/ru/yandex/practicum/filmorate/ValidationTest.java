package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTest {
    Film film = new Film();
    User user = new User();
    ValidationException exception;

    @BeforeEach
    public void setProperties() {
        film.setId(1);
        film.setDuration(10);
        film.setName("film");
        film.setDescription("new film");
        film.setReleaseDate(LocalDate.now());

        user.setId(1);
        user.setLogin("user");
        user.setBirthday(LocalDate.now());
        user.setName("name");
        user.setEmail("email@mail.ru");
    }

    @Test
    public void checkValidationFilmExceptionDuration() {
        film.setDuration(-1);
        exception = assertThrows(
                ValidationException.class,
                () -> validateFilm(film)
        );
        assertEquals("The duration of film is incorrect", exception.getMessage());
    }

    @Test
    public void checkValidationFilmExceptionDate() {
        film.setReleaseDate(LocalDate.of(999,12,5));
        exception = assertThrows(
                ValidationException.class,
                () -> validateFilm(film)
        );
        assertEquals("Films dont exist that time", exception.getMessage());
    }

    @Test
    public void checkValidationFilmExceptionDescription() {
        film.setDescription("toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo long description");
        exception = assertThrows(
                ValidationException.class,
                () -> validateFilm(film)
        );
        assertEquals("Too long description", exception.getMessage());
    }

    @Test
    public void checkValidationFilmExceptionName() {
        film.setName("");
        exception = assertThrows(
                ValidationException.class,
                () -> validateFilm(film)
        );
        assertEquals("The name is incorrect", exception.getMessage());
    }

    @Test
    public void checkValidationUserExceptionDate() {
        user.setBirthday(LocalDate.of(99999,9,8));
        exception = assertThrows(
                ValidationException.class,
                () -> validateUser(user)
        );
        assertEquals("Are you from the future?", exception.getMessage());
    }

    @Test
    public void checkValidationUserExceptionLogin() {
        user.setLogin("");
        exception = assertThrows(
                ValidationException.class,
                () -> validateUser(user)
        );
        assertEquals("Login is incorrect", exception.getMessage());
    }

    @Test
    public void checkValidationUserExceptionEmail() {
        user.setEmail("");
        exception = assertThrows(
                ValidationException.class,
                () -> validateUser(user)
        );
        assertEquals("Email is incorrect", exception.getMessage());
    }

    @Test
    public void checkEmptyName() {
        user.setName(null);
        User newUser = addUser(user);
        assertEquals(newUser.getName(), newUser.getLogin());
    }

    private void validateFilm (Film film) {
        if (film.getName().isBlank() || film.getName() == null)
            throw new ValidationException("The name is incorrect");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Too long description");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Films dont exist that time");
        if (film.getDuration() <= 0)
            throw new ValidationException("The duration of film is incorrect");
    }

    private void validateUser (User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("Email is incorrect");
        if (user.getLogin().isBlank() || user.getLogin() == null)
            throw new ValidationException("Login is incorrect");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Are you from the future?");
    }

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
        if (user.getId() == 0)
            user.setId(5);
        return user;
    }
}
