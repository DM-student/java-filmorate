package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private UserStorage users;

    public void addFriend(long userId, long targetId) {
        User user = users.getUser(userId);
        User target = users.getUser(targetId);

        if (user == null) {
            throw new NotFoundException("User ID" + userId);
        }
        if (target == null) {
            throw new NotFoundException("User ID" + targetId);
        }
        if (user.getFriends().contains(targetId)) {
            throw new IllegalArgumentException("Цель уже в друзьях у пользователя.");
        }
        user.getFriends().add(targetId);
        users.replaceUser(user);
    }

    public void removeFriend(long userId, long targetId) {
        User user = users.getUser(userId);
        User target = users.getUser(targetId);

        if (user == null) {
            throw new NotFoundException("User ID" + userId);
        }
        if (target == null) {
            throw new NotFoundException("User ID" + targetId);
        }
        if (!user.getFriends().contains(targetId)) {
            throw new NotFoundException("User ID" + userId + "`s friend with ID" + targetId);
        }
        user.getFriends().remove(targetId);
        users.replaceUser(user);
    }

    public List<User> getCommonFriends(long user, long otherUser) {
        Set<Long> userFriends = users.getUser(user).getFriends();
        Set<Long> otherUserFriends = users.getUser(otherUser).getFriends();

        return userFriends.stream().filter(otherUserFriends::contains).map((id) -> users.getUser(id))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<User> getFriends(long id) {
        return users.getUser(id).getFriends().stream().map(users::getUser).collect(Collectors.toUnmodifiableList());
    }

    public User getUser(long id) {
        return users.getUser(id);
    }

    public List<User> getUsers() {
        return users.getUsers();
    }

    public void addUser(User user) {
        users.addUser(user);
    }

    public void replaceUser(User user) {
        users.replaceUser(user);
    }

    @Autowired
    public UserService(UserStorage userStorage) {
        users = userStorage;
    }
}
