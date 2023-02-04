package ru.yandex.practicum.filmorate.exception;

public class UserIsAlreadyFriendException extends RuntimeException {
    public UserIsAlreadyFriendException(String message) {
        super(message);
    }
}

