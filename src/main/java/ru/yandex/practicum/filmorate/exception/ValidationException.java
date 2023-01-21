package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с валидацией
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
