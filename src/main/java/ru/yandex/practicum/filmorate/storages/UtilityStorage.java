package ru.yandex.practicum.filmorate.storages;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface UtilityStorage
{
	Genre getGenre(long id);
	List<Genre> getGenres();

	MPA getMpa(long id);
	List<MPA> getMpas();
}
