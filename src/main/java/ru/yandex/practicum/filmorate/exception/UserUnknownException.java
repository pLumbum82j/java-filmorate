package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с объектом user
 */
public class UserUnknownException extends RuntimeException {
    public UserUnknownException(String message) {
        super(message);
    }
}
