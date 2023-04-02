package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.DBUserStorage;

import java.time.LocalDate;
import java.util.HashSet;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
    // Формат тестов правильный же?

    private final DBUserStorage userStorage;

    public void setUp() {
        User user = new User(null, "some.mail@mymail.com", "myLogin",
                "Dug Dag", LocalDate.of(2001, 2, 3), new HashSet<>());
        userStorage.addUser(user);
    }

    @Test
    public void testFindUserById() {
        setUp();

        User user = userStorage.getUser(1);

        Assertions.assertEquals(user.getName(), "Dug Dag");
    }
}