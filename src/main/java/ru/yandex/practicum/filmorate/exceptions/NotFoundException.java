package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException
{
	String element;

	@Override
	public String getMessage()
	{
		return "Элемент не был найден. Информация об элементе: " + element;
	}

	public NotFoundException(String elementInfo)
	{
		element = elementInfo;
		log.error("Элемент не был найден. Информация об элементе: " + elementInfo);
	}
}
