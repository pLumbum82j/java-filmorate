package ru.yandex.practicum.filmorate.exception;

/**
 * Класс собственного исключения при работе с параметром
 */
public class IncorrectParameterException extends RuntimeException {

    public IncorrectParameterException(String message) {
        super(message);
    }

}
