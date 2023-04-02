package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbsupport.FilmDataBase;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;


@Component
@Primary
public class DBFilmStorage implements FilmStorage {
    @Autowired
    private FilmDataBase filmDataBase;

    @Override
    public Film getFilm(long id) {
        if (filmDataBase.getFilmById(id).isEmpty()) {
            throw new NotFoundException("Film ID" + id);
        }
        return filmDataBase.getFilmById(id).get();
    }

    @Override
    public List<Film> getFilms() {
        return filmDataBase.getAllFilms();
    }

    @Override
    public void addFilm(Film film) {
        filmDataBase.uploadFilm(film);
    }

    @Override
    public void replaceFilm(Film film) {
        if (film.getId() == null) {
            throw new IllegalArgumentException("При попытке заменить объект Film - идентификатор не был указан");
        }
        if (filmDataBase.getFilmById(film.getId()).isEmpty()) {
            throw new NotFoundException("Film ID" + film.getId());
        }
        filmDataBase.updateFilm(film);
    }
}
