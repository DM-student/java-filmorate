package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    @NotNull private String name;
    @NotNull private String description;
    @NotNull private LocalDate releaseDate;
    @NotNull private int duration;
    Set<Long> likes = new HashSet<>();
}