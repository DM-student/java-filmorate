package ru.yandex.practicum.filmorate;

import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
	public static final Validator validator =
			Validation.buildDefaultValidatorFactory().getValidator();
	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
	}

}
