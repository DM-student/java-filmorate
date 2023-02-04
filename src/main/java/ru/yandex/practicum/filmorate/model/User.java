package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User
{
	private int id;
	@NotNull @Email private String email;
	@NotNull private String login;
	@NotNull private String name;
	@NotNull private LocalDate birthday;
}