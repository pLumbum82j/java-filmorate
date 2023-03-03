package ru.yandex.practicum.filmorate.exception;

public class UpdateFilmUnknownException extends RuntimeException {
    public UpdateFilmUnknownException(String message) {
        super(message);
    }
}
