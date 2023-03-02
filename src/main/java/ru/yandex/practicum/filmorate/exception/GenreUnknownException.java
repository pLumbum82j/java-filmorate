package ru.yandex.practicum.filmorate.exception;

public class GenreUnknownException extends RuntimeException {
    public GenreUnknownException(String message) {
        super(message);
    }
}
