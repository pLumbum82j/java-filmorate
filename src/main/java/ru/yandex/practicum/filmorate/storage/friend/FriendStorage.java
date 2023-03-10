package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    /**
     * Метод получения списка друзей пользователя по id
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    List<User> getUserFriends(long id);

    /**
     * Метод получения списка общих друзей двух пользователей по id
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     * @return список общих друзей двух пользователей
     */
    List<User> getListOfCommonFriends(long firstId, long secondId);

    /**
     * Метод добавления пользователя в друзья
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    void addFriends(long firstId, long secondId);

    /**
     * Метод удаления пользователя из друзей
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    void deleteFriends(long firstId, long secondId);

}
