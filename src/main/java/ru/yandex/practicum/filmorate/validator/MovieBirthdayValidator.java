package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MovieBirthdayValidator implements ConstraintValidator<MovieBirthday, LocalDate>{


    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (localDate.isBefore(LocalDate.of(1895, 12, 28))) {
            return false;
        }
        return true;
    }

}
