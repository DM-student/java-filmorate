package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest
{
	Film film = new Film();

	@BeforeEach
	void beforeEach()
	{
		film.setName("Test");
		film.setDescription("Testing.");
		film.setDuration(Duration.ofHours(2));
		film.setReleaseDate(LocalDate.now());
	}
	@Test
	void testNameValidationFail()
	{
		film.setName("");
		assertThrows(ValidationException.class, () ->{
			FilmController.validate(film);
		});
	}
	@Test
	void testDescriptionValidationFail()
	{
		film.setDescription(String.valueOf(new char[300]));
		assertThrows(ValidationException.class, () ->{
			FilmController.validate(film);
		});
	}
	@Test
	void testDurationValidationFail()
	{
		film.setDuration(Duration.ofHours(-1));
		assertThrows(ValidationException.class, () ->{
			FilmController.validate(film);
		});
	}
	@Test
	void testDateValidationFail()
	{
		film.setReleaseDate(LocalDate.of(1500, 1, 1));
		assertThrows(ValidationException.class, () ->{
			FilmController.validate(film);
		});
	}
	@Test
	void testCloseCalls()
	{
		film.setName("-");
		film.setDescription(String.valueOf(new char[200]));
		film.setDuration(Duration.ofHours(0));
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		FilmController.validate(film);
	}
}