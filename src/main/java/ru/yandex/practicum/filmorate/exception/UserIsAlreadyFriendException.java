package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения, где пользователи уже друзья
 */
public class UserIsAlreadyFriendException extends RuntimeException {
    public UserIsAlreadyFriendException(String message) {
        super(message);
    }
}

