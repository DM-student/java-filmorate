package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidationException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Ошибка валидации.";
    }


    public ValidationException() {
        super();
        log.error("Произошла ошибка валидации.");
    }
}
