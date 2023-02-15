package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	@Autowired
	private UserStorage users;

	public UserStorage getUserStorage()
	{
		return users;
	}
	public boolean userIsPresent(long id)
	{
		return users.getUsers().contains(id);
	}
	public void addFriend(long userId, long targetId)
	{
		if(users.getUser(userId) == null)
		{
			throw new NullPointerException("Пользователь добавляющий в друзья не найден.");
		}
		if(users.getUser(targetId) == null)
		{
			throw new NullPointerException("Цель для добавления в друзья не найдена.");
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
			throw new NullPointerException("Пользователь добавляющий в друзья не найден.");
		}
		if(users.getUser(targetId) == null)
		{
			throw new NullPointerException("Цель для добавления в друзья не найдена.");
		}
		if(!users.getUser(userId).getFriends().contains(targetId))
		{
			throw new NullPointerException("Цели нет в друзьях у пользователя.");
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

	@Autowired
	public UserService(InMemoryUserStorage userStorage)
	{
		users = userStorage;
	}
}
