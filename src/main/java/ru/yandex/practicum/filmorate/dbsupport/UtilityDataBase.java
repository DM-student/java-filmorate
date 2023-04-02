package ru.yandex.practicum.filmorate.dbsupport;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UtilityDataBase {
    // Как я понял, не требуется реализация записи для рейтинга и жанров, но возможно я ошибаюсь.
    private final JdbcTemplate jdbcTemplate;

    public Optional<Genre> getGenreById(long id) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from genres where id = ?", id);

        // обрабатываем результат выполнения запроса
        if (sqlRows.next()) {
            Genre genre = new Genre(sqlRows.getLong("id"),
                    sqlRows.getString("name"));
            log.info("Найден жанр в БД: {} {}", genre.getId(), genre.getName());

            return Optional.of(genre);
        } else {
            log.info("Жанр с идентификатором {} не найден в БД.", id);
            return Optional.empty();
        }
    }

    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from genres");
        while (sqlRows.next()) {
            Genre genre = new Genre(sqlRows.getLong("id"),
                    sqlRows.getString("name"));
            genres.add(genre);
        }
        log.info("Возвращён список всех жанров размером: {}", genres.size());
        return genres;
    }

    public Set<Genre> getGenresForFilm(long filmId) {
        Set<Genre> genres = new HashSet<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from films_to_genres where film_id = ? order by genre_id", filmId);
        while (sqlRows.next()) {
            genres.add(getGenreById(sqlRows.getLong("genre_id")).get());
        }
        return genres;
    }

    public void uploadGenresForFilm(Film film) {
        String sqlQuery = "delete from films_to_genres where film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId()); // Это хоть и не самый лучший,
        // но самый простой способ избежать повторов.

        for (Long id : film.getGenres().stream().map(Genre::getId).collect(Collectors.toList())) {
            sqlQuery = "insert into films_to_genres(film_id, genre_id) " +
                    "values (?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), id);
        }
    }

    public Optional<MPA> getMpaById(long id) {
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from mpa where id = ?", id);

        // обрабатываем результат выполнения запроса
        if (sqlRows.next()) {
            MPA mpa = new MPA(sqlRows.getLong("id"),
                    sqlRows.getString("name"));
            log.info("Найден рейтинг в БД: {} {}", mpa.getId(), mpa.getName());

            return Optional.of(mpa);
        } else {
            log.info("Рейтинг с идентификатором {} не найден в БД.", id);
            return Optional.empty();
        }
    }


    public List<MPA> getAllMpa() {
        List<MPA> mpas = new ArrayList<>();
        SqlRowSet sqlRows = jdbcTemplate.queryForRowSet("select * from mpa");
        while (sqlRows.next()) {
            MPA mpa = new MPA(sqlRows.getLong("id"),
                    sqlRows.getString("name"));
            mpas.add(mpa);
        }
        log.info("Возвращён список всех рейтингов размером: {}", mpas.size());
        return mpas;
    }

    @Autowired
    public UtilityDataBase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
