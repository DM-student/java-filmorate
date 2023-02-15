package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.AnnotationValidator;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController
{
	@Autowired
	private AnnotationValidator annotationValidator;
	@Autowired
	private FilmService filmService;

	@GetMapping("/films")
	public List<Film> getFilms()
	{
		return filmService.getFilmStorage().getFilms();
	}
	@GetMapping("/films/{id}")
	public Film getFilm(@PathVariable long id)
	{
		if(filmService.getFilmStorage().getFilm(id) == null)
		{
			throw new NullPointerException("Фильм не найден.");
		}
		return filmService.getFilmStorage().getFilm(id);
	}
	@PostMapping("/films")
	public Film addFilm(@RequestBody Film film)
	{
		if(!isValid(film)) {throw new ValidationException();}
		filmService.getFilmStorage().addFilm(film);
		return film;
	}
	@PutMapping("/films")
	public Film ReplaceFilm(@RequestBody Film film)
	{
		if(!isValid(film)) {throw new ValidationException();}
		filmService.getFilmStorage().replaceFilm(film);
		return film;
	}

	@PutMapping("/films/{id}/like/{userId}")
	public void addLike(@PathVariable long id, @PathVariable long userId)
	{
		filmService.addLike(id, userId);
	}
	@DeleteMapping("/films/{id}/like/{userId}")
	public void removeLike(@PathVariable long id, @PathVariable long userId)
	{
		filmService.removeLike(id, userId);
	}
	@GetMapping("/films/popular")
	public List<Film> getPopular(@RequestParam(required = false) Integer count)
	{
		if(count == null) { count = 10; }
		return filmService.getPopular(count);
	}
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> validationException(ValidationException e)
	{
		return Map.of("error", "Ошибка валидации.");
	}
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, String> IllegalArgument(IllegalArgumentException e)
	{
		return Map.of("error", "Данный запрос не может быть обработан.");
	}
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String, String> nullPointer(NullPointerException e)
	{
		return Map.of("error", "Данные по запросу не найдены.");
	}

	public boolean isValid(Film film)
	{
		if(!annotationValidator.isValid(film)) { return false; }
		if(film.getName().isEmpty()) { return false; }
		if(film.getDescription().length() > 200) { return false; }
		if(film.getDuration() < 0) { return false; }
		if(film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) { return false; }
		return true;
	}
}
