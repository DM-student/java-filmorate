package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUser(long id);

    List<User> getUsers();

    void addUser(User user);

    void replaceUser(User user);
}
