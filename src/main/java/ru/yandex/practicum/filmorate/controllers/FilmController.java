package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController
{
	// Пока это будет так хранится.
	private Map<Integer, Film> films = new HashMap<>();
	private int lastId = 0;

	@GetMapping("/films")
	public List<Film> getFilms()
	{
		return new ArrayList<>(films.values());
	}
	@GetMapping("/films/{id}")
	public Film getFilm(@PathVariable int id)
	{
		return films.get(id);
	}
	@PostMapping("/films")
	public void addFilm(@RequestBody Film film)
	{
		films.put(lastId + 1, film);
		film.setId(lastId + 1);
		lastId++;
	}
	@PostMapping("/films/{id}")
	public void ReplaceFilm(@RequestBody Film film, @PathVariable int id)
	{
		films.replace(id, film);
		film.setId(id);
	}

	public static boolean validate(Film film)
	{
		if(!FilmorateApplication.validator.validate(film).isEmpty())
		{
			throw new ValidationException();
		}

		if(film.getName().isEmpty()) { throw new ValidationException(); }
		if(film.getDescription().length() > 200) { throw new ValidationException(); }
		if(film.getDuration().isNegative()) { throw new ValidationException(); }
		if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))  { throw new ValidationException();}

		return true;
	}
}