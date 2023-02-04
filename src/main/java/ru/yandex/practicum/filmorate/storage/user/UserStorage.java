package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage {
    Map<Long, User> getUsers();

    User create(User user);

    User update(User user);

    User findUserById(Long id);

    boolean isContainUserId(Long id);
}
