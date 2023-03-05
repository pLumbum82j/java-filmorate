package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с объектом genre
 */
public class GenreUnknownException extends RuntimeException {
    public GenreUnknownException(String message) {
        super(message);
    }
}
