package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationTest {

    Film film1 = new Film(1, "name", "description", LocalDate.now(), Duration.ZERO);
    Film film2 = new Film(2, "name", "description", LocalDate.of(999, 9, 9), Duration.ofMinutes(504));
    Film film3 = new Film(3, "name", "toooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo long description",
                LocalDate.now(), Duration.ofMinutes(10));
    Film film4 = new Film(4, "", "description", LocalDate.now(), Duration.ofMinutes(10));
    User user1 = new User(1,"email@mail.ru","login", "name", LocalDate.of(9000,9,8));
    User user2 = new User(2, "email@mail.ru", "", "name", LocalDate.now());
    User user3 = new User(3, "", "login", "name", LocalDate.now());

    @Test
    public void checkValidationFilmExceptions() {

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> validateFilm(film1)
        );
        assertEquals("The duration of film is incorrect", exception1.getMessage());

        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> validateFilm(film2)
        );
        assertEquals("Films dont exist that time", exception2.getMessage());

        final ValidationException exception3 = assertThrows(
                ValidationException.class,
                () -> validateFilm(film3)
        );
        assertEquals("Too long description", exception3.getMessage());

        final ValidationException exception4 = assertThrows(
                ValidationException.class,
                () -> validateFilm(film4)
        );
        assertEquals("The name is incorrect", exception4.getMessage());
    }

    @Test
    public void checkValidationUserExceptions() {

        final ValidationException exception1 = assertThrows(
                ValidationException.class,
                () -> validateUser(user1)
        );
        assertEquals("Are you from the future?", exception1.getMessage());

        final ValidationException exception2 = assertThrows(
                ValidationException.class,
                () -> validateUser(user2)
        );
        assertEquals("Login is incorrect", exception2.getMessage());

        final ValidationException exception3 = assertThrows(
                ValidationException.class,
                () -> validateUser(user3)
        );
        assertEquals("Email is incorrect", exception3.getMessage());
    }

    private void validateFilm (Film film) {
        if (film.getName().isBlank())
            throw new ValidationException("The name is incorrect");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Too long description");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28)))
            throw new ValidationException("Films dont exist that time");
        if (film.getDuration().isNegative() || film.getDuration().isZero() || film.getDuration() == null)
            throw new ValidationException("The duration of film is incorrect");
    }

    private void validateUser (User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@"))
            throw new ValidationException("Email is incorrect");
        if (user.getLogin().isBlank())
            throw new ValidationException("Login is incorrect");
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("Are you from the future?");
    }
}
