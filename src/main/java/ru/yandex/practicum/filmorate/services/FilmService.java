package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storages.FilmStorage;
import ru.yandex.practicum.filmorate.storages.InMemoryFilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService
{
	private FilmStorage films;
	@Autowired
	private UserService userService;

	public FilmStorage getFilmStorage()
	{
		return films;
	}
	public void addLike(long filmId, long userId)
	{
		if(films.getFilm(filmId) == null)
		{
			throw new NullPointerException("Данный фильм не найден.");
		}
		if(films.getFilm(filmId) == null)
		{
			throw new NullPointerException("Данный фильм не найден.");
		}
		if(userService.userIsPresent(userId))
		{
			throw new IllegalArgumentException("Пользователя с таким ID не найден.");
		}
		films.getFilm(filmId).getLikes().add(userId);
	}
	public void removeLike(long filmId, long userId)
	{
		if(films.getFilm(filmId) == null)
		{
			throw new NullPointerException("Данный фильм не найден.");
		}
		if(userService.userIsPresent(userId))
		{
			throw new NullPointerException("Пользователя с таким ID не найден.");
		}
		if(!films.getFilm(filmId).getLikes().contains(userId))
		{
			throw new NullPointerException("Данный фильм не имеет лайка от данного пользователя.");
		}
	}
	public List<Film> getPopular(int count)
	{
		return films.getFilms().stream().sorted(Comparator.comparingInt(x -> -x.getLikes().size()))
				.limit(count).collect(Collectors.toUnmodifiableList());
	}

	@Autowired
	public FilmService(InMemoryFilmStorage filmStorage)
	{
		films = filmStorage;
	}
}
