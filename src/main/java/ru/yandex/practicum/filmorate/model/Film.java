package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull private String name;
    @NotNull private String description;
    @NotNull private LocalDate releaseDate;
    @NotNull private Duration duration;
}
