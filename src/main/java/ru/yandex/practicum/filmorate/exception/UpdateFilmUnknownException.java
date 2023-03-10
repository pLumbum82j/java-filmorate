package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с обновлением объекта film
 */
public class UpdateFilmUnknownException extends RuntimeException {
    public UpdateFilmUnknownException(String message) {
        super(message);
    }
}
