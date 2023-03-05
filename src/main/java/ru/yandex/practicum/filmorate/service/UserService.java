package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    /**
     * Метод получения списка пользователей
     *
     * @return Список пользователей
     */
    List<User> getUsers();

    /**
     * Метод получения пользователя по id
     *
     * @param id id пользователя
     * @return объект пользователя
     */
    User findUserById(Long id);

    /**
     * Метод получения списка друзей пользователя по id
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    List<User> getUserFriends(Long id);

    /**
     * Метод получения списка общих друзей двух пользователей по id
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     * @return список общих друзей двух пользователей
     */
    List<User> getListOfCommonFriends(long firstId, long secondId);

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
     * Метод добавления пользователя в друзья
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    void addFriend(Long firstId, Long secondId);

    /**
     * Метод удаления пользователя из друзей
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    void deleteFriend(Long firstId, Long secondId);
}
