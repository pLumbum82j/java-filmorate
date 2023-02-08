package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения где пользователи не друзья
 */
public class UsersAreNotFriendsException extends RuntimeException {

    public UsersAreNotFriendsException(String message) {
        super(message);
    }
}
