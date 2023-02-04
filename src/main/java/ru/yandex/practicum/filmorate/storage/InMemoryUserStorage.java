package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    /**
     * Генератор ID
     *
     * @return ID
     */
    private Long generatorId() {
        return id++;
    }

    /**
     * Метод (эндпоинт) получения списка пользователей
     *
     * @return Список пользователей
     */

    public Map<Long, User> getUsers() {
        return users;
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */

    public User create(User user) {
        user.setId(generatorId());
        users.put(user.getId(), user);
        return user;
    }

    /**
     * Метод (эндпоинт) обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    public User update(User user) {
        return users.put(user.getId(), user);
    }
}
