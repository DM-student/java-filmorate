package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.DataBaseUserStorage;

import java.time.LocalDate;
import java.util.HashSet;


@SpringBootTest
@AutoConfigureTestDatabase
class FilmorateApplicationUserDBTests {

    @Autowired
    private DataBaseUserStorage userStorage;
    private static boolean initialized = false; // Из-за механизма использования BeforeAll, мне *намного*
    // проще использовать статичную переменную и BeforeEach.

    @BeforeEach
    void setUp() {
        User user1 = new User(1L, "some.mail@mymail.com", "myLogin",
                "Dug Dag", LocalDate.of(2001, 2, 3), new HashSet<>());
        User user2 = new User(2L, "secondone@mymail.com", "only-him",
                "Not Dag", LocalDate.of(2001, 3, 2), new HashSet<>());
        if (initialized) {
            userStorage.replaceUser(user1);
            userStorage.replaceUser(user2);
            return;
        }
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        initialized = true;
    }

    @Test
    void testFindUserCreationAndSearch() {
        User user = userStorage.getUser(1);
        Assertions.assertEquals(user.getName(), "Dug Dag");
    }

    @Test
    void testFailFindUserById() {

        Assertions.assertThrows(NotFoundException.class, () ->
                userStorage.getUser(99999));
    }

    @Test
    void testUserReplacement() {
        User newUser = new User(2L, "Someone@mymail.com", "Someone",
                "Somebody Else", LocalDate.of(2001, 1, 1), new HashSet<>());
        userStorage.replaceUser(newUser);

        Assertions.assertEquals(userStorage.getUser(2).getName(), "Somebody Else");
    }
}