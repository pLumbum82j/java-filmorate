package ru.yandex.practicum.filmorate.exception;

public class UserUnknownException extends RuntimeException {
    public UserUnknownException(String message) {
        super(message);
    }
}
