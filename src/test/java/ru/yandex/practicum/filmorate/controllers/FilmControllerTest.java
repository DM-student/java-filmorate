package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
Ввиду изменения принципа работы валидации, данные тесты временно убраны.

class FilmControllerTest
{
	Film film = new Film();

	@BeforeEach
	void beforeEach()
	{
		film.setName("Test");
		film.setDescription("Testing.");
		film.setDuration(100);
		film.setReleaseDate(LocalDate.now());
	}
	@Test
	void testNameValidationFail()
	{
		film.setName("");
		assertFalse(FilmController.isValid(film));
	}
	@Test
	void testDescriptionValidationFail()
	{
		film.setDescription(String.valueOf(new char[300]));
		assertFalse(FilmController.isValid(film));
	}
	@Test
	void testDurationValidationFail()
	{
		film.setDuration(-100);
		assertFalse(FilmController.isValid(film));
	}
	@Test
	void testDateValidationFail()
	{
		film.setReleaseDate(LocalDate.of(1500, 1, 1));
		assertFalse(FilmController.isValid(film));
	}
	@Test
	void testCloseCalls()
	{
		film.setName("-");
		film.setDescription(String.valueOf(new char[200]));
		film.setDuration(0);
		film.setReleaseDate(LocalDate.of(1895, 12, 28));
		assertTrue(FilmController.isValid(film));
	}
}

*/