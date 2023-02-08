package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest
{
	User user = new User();

	@BeforeEach
	void beforeEach()
	{
		user.setName("Tester Testov");
		user.setLogin("test");
		user.setEmail("test@testmail.com");
		user.setBirthday(LocalDate.of(2000, 1, 1));
	}

	@Test
	void testEmptyLoginValidationFail()
	{
		user.setLogin("");
		assertThrows(ValidationException.class, () ->{
			UserController.validate(user);
		});
	}
	@Test
	void testLoginWithSpacesValidationFail()
	{
		user.setLogin("te st");
		assertThrows(ValidationException.class, () ->{
			UserController.validate(user);
		});
	}
	@Test
	void testEmailValidationFail()
	{
		user.setEmail("notmail");
		assertThrows(ValidationException.class, () ->{
			UserController.validate(user);
		});
	}
	@Test
	void testBirthdayValidationFail()
	{
		user.setBirthday(LocalDate.MAX);
		assertThrows(ValidationException.class, () ->{
			UserController.validate(user);
		});
	}
	@Test
	void CloseCalls()
	{
		user.setBirthday(LocalDate.now());
		user.setName("");
		user.setLogin("te");
		user.setEmail("some@mail.com");
		UserController.validate(user);
	}
}