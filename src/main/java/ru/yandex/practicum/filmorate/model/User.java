package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User
{
	private long id;
	@NotNull @Email private String email;
	@NotNull private String login;
	private String name;
	@NotNull private LocalDate birthday;
	Set<Long> friends = new HashSet<>();
}