package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotNull private String name;
    @NotNull private String description;
    @NotNull private LocalDate releaseDate;
    @NotNull private int duration;
    private MPA mpa;
    private Integer rate; // тесты в постмане требуют этого поля...
    private Set<Genre> genres = new HashSet<>();
    private Set<Long> likes = new HashSet<>();
}