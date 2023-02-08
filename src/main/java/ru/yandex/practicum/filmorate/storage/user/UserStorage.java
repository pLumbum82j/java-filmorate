package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

/**
 * Интерфейс отвечаюзий за поведение объектов Film в памяти
 */
public interface UserStorage {

    /**
     * Метод получения списка пользователей
     *
     * @return Список пользователей
     */
    Map<Long, User> getUsers();

    /**
     * Метод получения пользователя по id
     *
     * @param id id пользователя
     * @return объект пользователя
     */
    User findUserById(Long id);

    /**
     * Метод создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    User create(User user);

    /**
     * Метод обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    User update(User user);

    /**
     * Метод проверки пользователя на сервере
     *
     * @param id id пользователя
     * @return Возвращаем true/false при прохождении валидации
     */
    boolean isContainUserId(Long id);
}
