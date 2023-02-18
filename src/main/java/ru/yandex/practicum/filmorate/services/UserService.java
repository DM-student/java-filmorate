package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storages.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storages.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService
{
	private UserStorage users;

	public boolean userIsPresent(long id)
	{
		return users.getUsers().contains(id);
	}
	public void addFriend(long userId, long targetId)
	{
		if(users.getUser(userId) == null)
		{
			throw new NotFoundException("User ID" + userId);
		}
		if(users.getUser(targetId) == null)
		{
			throw new NotFoundException("User ID" + targetId);
		}
		if(users.getUser(userId).getFriends().contains(targetId))
		{
			throw new IllegalArgumentException("Цель уже в друзьях у пользователя.");
		}
		users.getUser(userId).getFriends().add(targetId);
		users.getUser(targetId).getFriends().add(userId);
	}
	public void removeFriend(long userId, long targetId)
	{
		if(users.getUser(userId) == null)
		{
			throw new NotFoundException("User ID" + userId);
		}
		if(users.getUser(targetId) == null)
		{
			throw new NotFoundException("User ID" + targetId);
		}
		if(!users.getUser(userId).getFriends().contains(targetId))
		{
			throw new NotFoundException("User ID" + userId +"`s friend with ID" + targetId);
		}
		users.getUser(userId).getFriends().remove(targetId);
		users.getUser(targetId).getFriends().remove(userId);
	}
	public List<User> getCommonFriends(long user1, long user2)
	{
		Set<Long> user1Friends = users.getUser(user1).getFriends();
		Set<Long> user2Friends = users.getUser(user2).getFriends();

		return user1Friends.stream().filter(user2Friends::contains).map((id) -> users.getUser(id))
				.collect(Collectors.toUnmodifiableList());
	}
	public List<User> getFriends(long id)
	{
		return users.getUser(id).getFriends().stream().map(users::getUser).collect(Collectors.toUnmodifiableList());
	}

	public User getUser(long id)
	{
		return users.getUser(id);
	}
	public List<User> getUsers()
	{
		return users.getUsers();
	}
	public void addUser(User user)
	{
		users.addUser(user);
	}
	public void replaceUser(User user)
	{
		users.replaceUser(user);
	}

	@Autowired
	public UserService(InMemoryUserStorage userStorage)
	{
		users = userStorage;
	}
}
