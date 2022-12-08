package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final GenreDbStorage genreStorage;
	private final MpaDbStorage mpaStorage;

	private final Genres genre = new Genres(1, "Комедия");
	private final Mpa mpa = new Mpa(1, "G");
	private final User user = new User(1, "email@mail.ru", "login", "name", LocalDate.of(2000,3,1));
	private final User friend = new User(2, "email@mail.ru", "friend", "friendName", LocalDate.of(2000,3,1));
	private final Film film = new Film(1, "name", "description", LocalDate.of(2022, 2,2), 20,
			List.of(genre), mpa);
	private final Film film2 = new Film(2, "name2", "description", LocalDate.of(2022,2,2), 20,
			List.of(genre), mpa);

	@BeforeEach
	public void doBefore() {
		filmStorage.addFilm(film);
		filmStorage.addFilm(film2);
		userStorage.addUser(user);
		userStorage.addUser(friend);
	}

	@Test
	public void testGetUser() {
		Optional<User> userOptional = Optional.ofNullable(userStorage.getUser(1));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->
						assertThat(user).hasFieldOrPropertyWithValue("id", 1)
				);
	}

	@Test
	public void testGetALLUsers() {
		List<User> users = userStorage.getUsers();
		assertThat(users)
				.isNotEmpty();
		assertThat(users.get(0))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testGetFriends() {
		List<User> friends = userStorage.getAllFriends(1);
		assertThat(friends)
				.isEmpty();
		userStorage.addFriend(1,2);
		assertThat(userStorage.getAllFriends(1))
				.hasSize(1);
	}

	@Test
	public void testRemoveFriend() {
		userStorage.addFriend(1, 2);
		List<User> friends = userStorage.getAllFriends(1);
		assertThat(friends)
				.hasSize(2);
		assertThat(friends.get(1))
				.hasFieldOrPropertyWithValue("id", 2);
		userStorage.removeFriend(1, 2);
		friends = userStorage.getAllFriends(1);
		assertThat(friends)
				.isEmpty();
	}

	@Test
	public void testGetFilm() {
		assertThat(filmStorage.getFilm(1))
				.isNotNull()
				.hasFieldOrPropertyWithValue("name", "name");
	}

	@Test
	public void testGetAllFilms() {
		assertThat(filmStorage.getFilms().get(0))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1);
	}

	@Test
	public void testLikes() {
		assertThat(filmStorage.getMostPopularFilms(2))
				.hasSize(2);
		filmStorage.addLike(1, 2);
		List<Film> popFilms = filmStorage.getMostPopularFilms(1);
		assertThat(popFilms)
				.hasSize(1);
		assertThat(popFilms.get(0))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 2);
		filmStorage.removeLike(1,1);
		popFilms = filmStorage.getMostPopularFilms(1);
		assertThat(popFilms)
				.hasSize(1);
		assertThat(popFilms.get(0))
				.isNotNull();
	}

	@Test
	public void testMPA() {
		assertThat(mpaStorage.getMpa(1))
				.isNotNull()
				.hasFieldOrPropertyWithValue("id", 1);
		assertThat(mpaStorage.getRatings())
				.isNotNull()
				.hasSize(5);
	}

	@Test
	public void testGenres() {
		assertThat(genreStorage.getGenre(1))
				.isNotNull()
				.isEqualTo(genre);
		assertThat(genreStorage.getGenres())
				.isNotNull()
				.hasSize(6)
				.contains(genre);
	}
}
