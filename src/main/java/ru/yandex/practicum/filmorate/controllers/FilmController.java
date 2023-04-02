package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.AnnotationValidator;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    @Autowired
    private AnnotationValidator annotationValidator;
    @Autowired
    private FilmService filmService;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable long id) {
        return filmService.getFilm(id);
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        if (!isValid(film)) {
            throw new ValidationException();
        }
        filmService.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film replaceFilm(@RequestBody Film film) {
        if (!isValid(film)) {
            throw new ValidationException();
        }
        filmService.replaceFilm(film);
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) // Спасибо за совет.
    {
        return filmService.getPopular(count);
    }

    private boolean isValid(Film film) {
        if (!annotationValidator.isValid(film)) {
            return false;
        }
        if (film.getName().isEmpty()) {
            return false;
        }
        if (film.getDescription().length() > 200) {
            return false;
        }
        if (film.getDuration() < 0) {
            return false;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        }
        return true;
    }
}
