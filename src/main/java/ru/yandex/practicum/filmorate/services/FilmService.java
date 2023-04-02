package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService
{
	private FilmStorage films;
	private UserStorage users;

	public void addLike(long filmId, long userId)
	{
		Film film = films.getFilm(filmId);
		User user = users.getUser(userId);

		if(film == null)
		{
			throw new NotFoundException("Film ID" + filmId);
		}
		if(user == null)
		{
			throw new NotFoundException("User ID" + userId);
		}
		if(film.getLikes().contains(userId))
		{
			throw new IllegalArgumentException("Данный фильм уже имеет лайк от данного пользователя.");
		}
		film.getLikes().add(userId);
		replaceFilm(film);
	}
	public void removeLike(long filmId, long userId)
	{
		Film film = films.getFilm(filmId);
		User user = users.getUser(userId);

		if(film == null)
		{
			throw new NotFoundException("Film ID" + filmId);
		}
		if(user == null)
		{
			throw new NotFoundException("User ID" + userId);
		}
		if(!film.getLikes().contains(userId))
		{
			throw new NotFoundException("Like from User ID" + userId);
		}
		film.getLikes().remove(userId);
		replaceFilm(film);
	}
	public List<Film> getPopular(int count)
	{
		// Наверное стоит перенести поиск популярных фильмов в запрос SQL, или так тоже сойдёт?
		// Просто для этого придётся менять интерфейс хранилища, добавляя новый метод.
		return films.getFilms().stream().sorted(Comparator.comparingInt(x -> -x.getLikes().size()))
				.limit(count).collect(Collectors.toUnmodifiableList());
	}

	public Film getFilm(long id)
	{
		return films.getFilm(id);
	}
	public List<Film> getFilms()
	{
		return films.getFilms();
	}
	public void addFilm(Film film)
	{
		films.addFilm(film);
	}
	public void replaceFilm(Film film)
	{
		films.replaceFilm(film);
	}
	@Autowired
	public FilmService(FilmStorage filmStorage, UserStorage usersStorage)
	{
		films = filmStorage;
		users = usersStorage;
	}
}
