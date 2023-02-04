package ru.yandex.practicum.filmorate.exceptions;

public class ValidationException extends RuntimeException
{
	@Override
	public String getMessage()
	{
		return "Ошибка валидации.";
	}
}
