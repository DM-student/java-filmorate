package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Более не актуально.
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    Map<Long, Film> films = new HashMap<>();
    private long lastId = 0;

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms() {
        return List.copyOf(films.values());
    }

    @Override
    public List<Film> getPopularFilms(int limit) {
        return null;
    }

    @Override
    public void addFilm(Film film) {
        films.put(lastId + 1, film);
        film.setId(lastId + 1);
        log.info("Фильм был добавлен, его номер: " + lastId);
        lastId++;
    }

    @Override
    public void replaceFilm(Film film) {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException("Film ID" + film.getId());
        }
        films.replace(film.getId(), film);
        log.info("Фильм номер " + film.getId() + " был обновлён.");
    }
}
