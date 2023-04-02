package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbsupport.UserDataBase;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
@Primary
public class DBUserStorage implements UserStorage {
    @Autowired
    UserDataBase userDataBase;

    @Override
    public User getUser(long id) {
        if (userDataBase.getUserById(id).isEmpty()) {
            throw new NotFoundException("User ID" + id);
        }
        return userDataBase.getUserById(id).get();
    }

    @Override
    public List<User> getUsers() {
        return userDataBase.getAllUsers();
    }

    @Override
    public void addUser(User user) {
        userDataBase.uploadUser(user);
    }

    @Override
    public void replaceUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("При попытке заменить объект User - идентификатор не был указан");
        }
        if (userDataBase.getUserById(user.getId()).isEmpty()) {
            throw new NotFoundException("User ID" + user.getId());
        }
        userDataBase.updateUser(user);
    }
}
