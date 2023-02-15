package ru.yandex.practicum.filmorate.storages;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage
{
	private Map<Long, User> users = new HashMap<>();
	private long lastId = 0;

	@Override
	public User getUser(long id)
	{
		return users.get(id);
	}

	@Override
	public List<User> getUsers()
	{
		return List.copyOf(users.values());
	}

	@Override
	public void addUser(User user)
	{
		users.put(lastId + 1, user);
		user.setId(lastId + 1);
		lastId++;
		log.info("Пользователь был добавлен, его номер: " + lastId);
	}

	@Override
	public void replaceUser(User user)
	{
		if (users.get(user.getId()) == null)
		{
			throw new NullPointerException("Пользователь не найден.");
		}
		users.replace(user.getId(), user);
		log.info("Пользователь номер " + user.getId() + " был обновлён.");
	}
}
