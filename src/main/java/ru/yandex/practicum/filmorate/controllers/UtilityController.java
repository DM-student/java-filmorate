package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.services.UtilityService;

import java.util.List;

@RestController
public class UtilityController {
    @Autowired
    UtilityService utilityService;

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable long id) {
        return utilityService.getGenre(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return utilityService.getGenres();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMpa(@PathVariable long id) {
        return utilityService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getMpas() {
        return utilityService.getMpas();
    }
}
