package ru.yandex.practicum.filmorate.exception;

public class UsersAreNotFriendsException extends RuntimeException {

    public UsersAreNotFriendsException(String message) {
        super(message);
    }
}
