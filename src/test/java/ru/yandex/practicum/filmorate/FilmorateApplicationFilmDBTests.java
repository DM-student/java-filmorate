package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.DataBaseFilmStorage;
import ru.yandex.practicum.filmorate.storages.DataBaseUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmorateApplicationFilmDBTests {
    @Autowired
    private DataBaseFilmStorage filmStorage;
    @Autowired
    private DataBaseUserStorage userStorage;
    private static boolean initialized = false;

    @BeforeEach
    void setUp() {
        Film film1 = new Film(1L, "Какой-то там фильм.", "Очень-очень интересный фильм про каких-то зомби.",
                LocalDate.of(2020, 4, 2), 120, new MPA(1L, ""), 4,
                new HashSet<>(), Set.of(1L));
        Film film2 = new Film(2L, "Ещё один фильм.", "Не интересный фильм про зомби.",
                LocalDate.of(2020, 4, 2), 120, new MPA(1L, ""), 4,
                new HashSet<>(), new HashSet<>());
        User user = new User(1L, "film.critic@nonmail.com", "critic123",
                "Critic Smith", LocalDate.of(2001, 2, 1), new HashSet<>());
        if (initialized) {
            filmStorage.replaceFilm(film1);
            filmStorage.replaceFilm(film2);
            return;
        }
        userStorage.addUser(user);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);
        initialized = true;
    }

    @Test
    void testFilmCreationAndSearch() {
        Assertions.assertEquals(filmStorage.getFilm(1).getName(), "Какой-то там фильм.");
    }

    @Test
    void testFailFilmSearch() {
        Assertions.assertThrows(NotFoundException.class, () ->
                filmStorage.getFilm(9999));
    }

    @Test
    void testFilmReplacement() {
        Film film = new Film(2L, "Точно другой фильм", "Что это такое?",
                LocalDate.of(2021, 7, 4), 111, new MPA(1L, ""), 4,
                new HashSet<>(), new HashSet<>());
        filmStorage.replaceFilm(film);

        Assertions.assertEquals(filmStorage.getFilm(2).getName(), "Точно другой фильм");
    }

    @Test
    void testGetPopularFilm() {
        Assertions.assertEquals(filmStorage.getPopularFilms(1).get(0).getName(), "Какой-то там фильм.");
    }
}
