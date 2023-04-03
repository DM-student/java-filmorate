package ru.yandex.practicum.filmorate.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.Map;

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<Map<String, String>> getResponseEntity(Throwable e) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e.getClass() == ValidationException.class) {
            status = HttpStatus.BAD_REQUEST;
        }
        if (e.getClass() == IllegalArgumentException.class) {
            status = HttpStatus.BAD_REQUEST;
        }
        if (e.getClass() == NotFoundException.class) {
            status = HttpStatus.NOT_FOUND;
        }

        return new ResponseEntity<>(
                Map.of("error", e.getClass().getSimpleName(),
                        "errorInfo", e.getMessage()),
                status
        );
    }
}
