package ru.yandex.practicum.filmorate.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage
{
	Film getFilm(long id);
	List<Film> getFilms();
	void addFilm(Film film);
	void replaceFilm(Film film);
}
