package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Primary
@Slf4j
public class DataBaseFilmStorage implements FilmStorage {
    @Autowired
    private DataBaseUtilityStorage dataBaseUtilityStorage;
    private final JdbcTemplate jdbcTemplate;

    public long getLastId() {
        long id = 0;

        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films order by id desc");
        if (sqlRows.next()) {
            id = sqlRows.getLong("id");
        }
        return id;
    }

    @Override
    public Film getFilm(long id) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films where id = ?", id);

        // обрабатываем результат выполнения запроса
        if (sqlRows.next()) {
            Film film = new Film(sqlRows.getLong("id"),
                    sqlRows.getString("name"),
                    sqlRows.getString("description"),
                    sqlRows.getDate("release_date").toLocalDate(),
                    sqlRows.getInt("duration"),
                    dataBaseUtilityStorage.getMpa(sqlRows.getLong("mpa_id")),
                    sqlRows.getInt("rate"),
                    dataBaseUtilityStorage.getGenresForFilm(sqlRows.getLong("id")),
                    getLikes(sqlRows.getLong("id")));
            log.info("Найден фильм в БД: {} {}", film.getId(), film.getName());

            return film;
        } else {
            log.info("Фильм с идентификатором {} не найден в БД.", id);
            throw new NotFoundException("Film ID" + id);
        }
    }

    @Override
    public void addFilm(Film film) {
        String sqlQuery = "insert into films(id, name, description, release_date, duration, mpa_id, rate) " +
                "values (?, ?, ?, ?, ?, ?, ?)";
        film.setId(getLastId() + 1);
        jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getDescription(),
                Date.valueOf(film.getReleaseDate()), film.getDuration(), film.getMpa().getId(), film.getRate());
        uploadLikes(film);
        dataBaseUtilityStorage.uploadGenresForFilm(film);
        film.setMpa(dataBaseUtilityStorage.getMpa(film.getMpa().getId()));
        film.setGenres(dataBaseUtilityStorage.getGenresForFilm(film.getId()));
        log.info("Новый фильм создан в БД.");
    }

    @Override
    public void replaceFilm(Film film) {
        if (film.getId() == null) {
            throw new IllegalArgumentException("При попытке заменить объект Film - идентификатор не был указан");
        }
        getFilm(film.getId()); // Если такая запись не будет найдена - выкинет ошибку 404.

        String sqlQuery = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, rate = ? where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        uploadLikes(film);
        dataBaseUtilityStorage.uploadGenresForFilm(film);
        film.setMpa(dataBaseUtilityStorage.getMpa(film.getMpa().getId()));
        film.setGenres(dataBaseUtilityStorage.getGenresForFilm(film.getId()));
        log.info("Фильм под номером {} был обновлён.", film.getId());
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films");
        while (sqlRows.next()) {
            Film film = new Film(sqlRows.getLong("id"),
                    sqlRows.getString("name"),
                    sqlRows.getString("description"),
                    sqlRows.getDate("release_date").toLocalDate(),
                    sqlRows.getInt("duration"),
                    dataBaseUtilityStorage.getMpa(sqlRows.getLong("mpa_id")),
                    sqlRows.getInt("rate"),
                    dataBaseUtilityStorage.getGenresForFilm(sqlRows.getLong("id")),
                    getLikes(sqlRows.getLong("id")));
            films.add(film);
        }
        log.info("Возвращён список всех фильмов размером: {}", films.size());
        return films;
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        List<Film> films = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("SELECT a.id, count(b.like_id) AS likes " +
                "FROM films AS a " +
                "LEFT JOIN films_to_likes AS b ON a.ID = b.FILM_ID " +
                "GROUP BY a.id " +
                "ORDER BY likes desc " +
                "LIMIT ?", limit);
        while (sqlRows.next()) {
            films.add(getFilm(sqlRows.getLong("id")));
        }
        log.info("Возвращён список популярных фильмов размером: {}", films.size());
        return films;
    }

    public Set<Long> getLikes(long id) {
        Set<Long> likes = new HashSet<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films_to_likes where film_id = ?", id);
        while (sqlRows.next()) {
            likes.add(sqlRows.getLong("like_id"));
        }
        return likes;
    }

    public void uploadLikes(Film film) {
        String sqlQuery = "delete from films_to_likes where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId()); // Это хоть и не самый лучший,
        // но самый простой способ избежать повторов.

        for (Long id : film.getLikes()) {
            sqlQuery = "insert into films_to_likes(film_id, like_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), id);
        }
    }

    @Autowired
    public DataBaseFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
