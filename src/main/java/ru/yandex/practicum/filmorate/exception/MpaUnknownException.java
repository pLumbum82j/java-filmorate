package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с объектом mpa
 */
public class MpaUnknownException extends RuntimeException {
    public MpaUnknownException(String message) {
        super(message);
    }
}
