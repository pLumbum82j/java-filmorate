package ru.yandex.practicum.filmorate.exception;

public class FilmUnknownException extends RuntimeException {
    public FilmUnknownException(String message) {
        super(message);
    }
}
