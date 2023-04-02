package ru.yandex.practicum.filmorate.storages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dbsupport.UtilityDataBase;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

@Component
@Primary
public class DBUtilityStorage implements UtilityStorage {
    @Autowired
    private UtilityDataBase utilityDataBase;

    @Override
    public Genre getGenre(long id) {
        if (utilityDataBase.getGenreById(id).isEmpty()) {
            throw new NotFoundException("Genr ID" + id);
        }
        return utilityDataBase.getGenreById(id).get();
    }

    @Override
    public List<Genre> getGenres() {
        return utilityDataBase.getAllGenres();
    }

    @Override
    public MPA getMpa(long id) {

        if (utilityDataBase.getMpaById(id).isEmpty()) {
            throw new NotFoundException("Mpa ID" + id);
        }
        return utilityDataBase.getMpaById(id).get();
    }

    @Override
    public List<MPA> getMpas() {
        return utilityDataBase.getAllMpa();
    }
}
