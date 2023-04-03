package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@Slf4j
public class DataBaseUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public long getLastId() {
        long id = 0;
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from users order by id desc");
        if (sqlRows.next()) {
            id = sqlRows.getLong("id");
        }
        return id;
    }

    @Override
    public User getUser(long id) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from users where id = ?", id);

        // обрабатываем результат выполнения запроса
        if (sqlRows.next()) {
            User user = new User(
                    sqlRows.getLong("id"),
                    sqlRows.getString("email"),
                    sqlRows.getString("login"),
                    sqlRows.getString("name"),
                    // Тут конечно может выпасть NPE, но так или иначе это поле не должно быть Null.
                    sqlRows.getDate("birthday").toLocalDate(),
                    getFriends(id));
            log.info("Найден пользователь в БД: {} {}", user.getId(), user.getLogin());

            return user;
        } else {
            throw new NotFoundException("User ID" + id);
        }
    }

    @Override
    public void addUser(User user) {
        String sqlQuery = "insert into users(id, email, login, name, birthday) " +
                "values (?, ?, ?, ?, ?)";
        user.setId(getLastId() + 1);
        jdbcTemplate.update(sqlQuery, user.getId(), user.getEmail(), user.getLogin(),
                user.getName(), Date.valueOf(user.getBirthday()));
        uploadFriends(user);
        log.info("Новый пользователь создан в БД.");
    }

    @Override
    public void replaceUser(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("При попытке заменить объект User - идентификатор не был указан");
        }
        getUser(user.getId()); // Если такая запись не будет найдена - выкинет ошибку 404.


        String sqlQuery = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(),
                user.getName(), Date.valueOf(user.getBirthday()), user.getId());
        uploadFriends(user);
        log.info("Пользователь под номером {} был обновлён.", user.getId());
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from users");
        while (sqlRows.next()) {
            long id = sqlRows.getLong("id");

            User user = new User(
                    id,
                    sqlRows.getString("email"),
                    sqlRows.getString("login"),
                    sqlRows.getString("name"),
                    // Тут конечно может выпасть NPE, но так или иначе это поле не должно быть Null.
                    sqlRows.getDate("birthday").toLocalDate(),
                    getFriends(id));
            users.add(user);
        }
        log.info("Возвращён список всех пользователей размером:  {}", users.size());
        return users;
    }

    private Set<Long> getFriends(long id) {
        Set<Long> friends = new HashSet<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from users_to_friends where user_id = ?", id);
        while (sqlRows.next()) {
            friends.add(sqlRows.getLong("friend_id"));
        }
        return friends;
    }

    private void uploadFriends(User user) {
        String sqlQuery = "delete from users_to_friends where user_id = ?";
        jdbcTemplate.update(sqlQuery, user.getId()); // Это хоть и не самый лучший,
        // но самый простой способ избежать повторов.

        for (Long id : user.getFriends()) {
            sqlQuery = "insert into users_to_friends(user_id, friend_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, user.getId(), id);
        }
    }

    @Autowired
    public DataBaseUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}