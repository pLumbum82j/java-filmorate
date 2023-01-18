package ru.yandex.practicum.filmorate.exception;
/**
 * Класс собственного исключения при работе с объектом film
 */
public class FilmUnknownException extends RuntimeException {
    public FilmUnknownException(String message) {
        super(message);
    }
}
