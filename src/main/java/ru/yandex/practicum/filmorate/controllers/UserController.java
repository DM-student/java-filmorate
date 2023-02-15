package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.AnnotationValidator;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class UserController
{
	@Autowired
	private AnnotationValidator annotationValidator;
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public List<User> getUsers()
	{
		return userService.getUserStorage().getUsers();
	}
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long id)
	{
		if(userService.getUserStorage().getUser(id) == null)
		{
			throw new NullPointerException("Пользователь не найден.");
		}
		return userService.getUserStorage().getUser(id);
	}
	@PostMapping("/users")
	public User addUser(@RequestBody User user)
	{
		if(!isValid(user)) { throw new ValidationException(); }
		userService.getUserStorage().addUser(user);
		return user;
	}
	@PutMapping("/users")
	public User ReplaceUser(@RequestBody User user)
	{
		if(!isValid(user)) { throw new ValidationException(); }
		userService.getUserStorage().replaceUser(user);
		return user;
	}
	@PutMapping("/users/{id}/friends/{friendId}")
	public void addFriend(@PathVariable long id, @PathVariable long friendId)
	{
		userService.addFriend(id, friendId);
	}
	@DeleteMapping("/users/{id}/friends/{friendId}")
	public void removeFriend(@PathVariable long id, @PathVariable long friendId)
	{
		userService.removeFriend(id, friendId);
	}
	@GetMapping("/users/{id}/friends/common/{friendId}")
	public List<User> getCommonFriends(@PathVariable long id, @PathVariable long friendId)
	{
		return userService.getCommonFriends(id, friendId);
	}
	@GetMapping("/users/{id}/friends")
	public List<User> getFriends(@PathVariable long id)
	{
		return userService.getFriends(id);
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
	public boolean isValid(User user)
	{
		if(!annotationValidator.isValid(user)) { return false; }
		if(user.getName() == null || user.getName().isEmpty()) { user.setName(user.getLogin()); }
		if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) { return false; }
		if(user.getBirthday().isAfter(LocalDate.now())) { return false; }
		return true;
	}
}
