package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class UserController
{
	// Пока это будет так хранится.
	private Map<Integer, User> users = new HashMap<>();
	private int lastId = 0;

	@GetMapping("/users")
	public List<User> getUsers()
	{
		return new ArrayList<>(users.values());
	}
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable int id)
	{
		return users.get(id);
	}
	@PostMapping("/users")
	public void addUser(@RequestBody User user)
	{
		validate(user);
		users.put(lastId + 1, user);
		user.setId(lastId + 1);
		lastId++;
		log.info("Пользователь был добавлен, его номер: " + lastId);
	}
	@PutMapping("/users")
	public void ReplaceUser(@RequestBody User user)
	{
		validate(user);
		users.replace(user.getId(), user);
		log.info("Пользователь номер " + user.getId() + " был обновлён.");
	}

	public static boolean validate(User user)
	{
		if(!FilmorateApplication.validator.validate(user).isEmpty())
		{
			throw new ValidationException();
		}

		if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) { throw new ValidationException(); }
		if(user.getBirthday().isAfter(LocalDate.now())) { throw new ValidationException(); }
		return true;
	}
}
