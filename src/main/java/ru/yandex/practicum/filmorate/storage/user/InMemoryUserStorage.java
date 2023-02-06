package ru.yandex.practicum.filmorate.storage.user;

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

    public Map<Long, User> getUsers() {
        return users;
    }

    public User findUserById(Long id) {
        return users.get(id);
    }

    public User create(User user) {
        user.setId(generatorId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        return users.put(user.getId(), user);
    }

    public boolean isContainUserId(Long id) {
        return !users.containsKey(id);
    }
}
