package ru.yandex.practicum.filmorate.services;

import org.springframework.stereotype.Service;

import javax.validation.Validation;

@Service
public class AnnotationValidator {
    private javax.validation.Validator localValidator;

    public boolean isValid(Object target) {
        return localValidator.validate(target).isEmpty();
    }

    public AnnotationValidator() {
        localValidator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
