package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
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
		return userService.getUsers();
	}
	@GetMapping("/users/{id}")
	public User getUser(@PathVariable long id)
	{
		if(userService.getUser(id) == null)
		{
			throw new NotFoundException("User ID" + id);
		}
		return userService.getUser(id);
	}
	@PostMapping("/users")
	public User addUser(@RequestBody User user)
	{
		if(!isValid(user)) { throw new ValidationException(); }
		userService.addUser(user);
		return user;
	}
	@PutMapping("/users")
	public User replaceUser(@RequestBody User user)
	{
		if(!isValid(user)) { throw new ValidationException(); }
		userService.replaceUser(user);
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
	public ResponseEntity<Map<String, String>> errorHandler(Throwable e)
	{
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

		if(e.getClass() == ValidationException.class) { status = HttpStatus.BAD_REQUEST; }
		if(e.getClass() == IllegalArgumentException.class) { status = HttpStatus.BAD_REQUEST; }
		if(e.getClass() == NotFoundException.class) { status = HttpStatus.NOT_FOUND; }

		return new ResponseEntity<>(
				Map.of("error", e.getClass().getSimpleName(),
						"errorInfo", e.getMessage()),
				status
		);
	}
	private boolean isValid(User user)
	{
		if(!annotationValidator.isValid(user)) { return false; }
		if(user.getName() == null || user.getName().isEmpty()) { user.setName(user.getLogin()); }
		if(user.getLogin().isEmpty() || user.getLogin().contains(" ")) { return false; }
		if(user.getBirthday().isAfter(LocalDate.now())) { return false; }
		return true;
	}
}
