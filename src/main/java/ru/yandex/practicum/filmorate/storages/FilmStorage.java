package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilm(long id);

    List<Film> getFilms();

    List<Film> getPopularFilms(int limit);

    void addFilm(Film film);

    void replaceFilm(Film film);
}
