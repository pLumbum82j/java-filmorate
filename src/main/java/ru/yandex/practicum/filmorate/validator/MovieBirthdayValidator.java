package ru.yandex.practicum.filmorate.validator;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Service
public class MovieBirthdayValidator implements ConstraintValidator<MovieBirthday, LocalDate> {
    private LocalDate data;

    public void initialize(MovieBirthday annotation) {
        data = LocalDate.parse(annotation.value());
    }

    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return (value == null) || value.isAfter(data);
    }
}