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
    @NotNull private int duration; // Если я буду использовать Duration, то код не пройдёт тесты.
    // Ожидается что это значение будет отвечать за длину в минутах. Я конечно могу теоретически
    // написать свой код отвечающий за переработку объекта в JSON, но не уверен, потяну ли я такое.
}