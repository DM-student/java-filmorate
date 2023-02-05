package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private int id;
    @NotNull private String name;
    @NotNull private String description;
    @NotNull private LocalDate releaseDate;
    @NotNull private int duration;
    // К сожалению в ТЗ не указали тип переменной,
    // которая должна отвечать за продолжительность. Я выбрал int, исходя из ответов которые ожидаются.
}