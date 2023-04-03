package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storages.UtilityStorage;

import java.util.List;

@Service
public class UtilityService {
    @Autowired
    UtilityStorage utilityStorage;

    public MPA getMpa(long id) {
        return utilityStorage.getMpa(id);
    }

    public List<MPA> getMpas() {
        return utilityStorage.getMpas();
    }

    public Genre getGenre(long id) {
        return utilityStorage.getGenre(id);
    }

    public List<Genre> getGenres() {
        return utilityStorage.getGenres();
    }
}
