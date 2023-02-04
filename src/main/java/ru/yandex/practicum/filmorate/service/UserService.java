package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    /**
     * Метод (эндпоинт) получения списка пользователей
     *
     * @return Список пользователей
     */
    public List<User> getUsers() {
        log.debug("Получен запрос на список пользователей");
        return new ArrayList<>(userStorage.getUsers().values());
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    public User create(User user) {
        if (isValid(user)) {
            if ((user.getName() == null) || (user.getName().isBlank())) {
                user.setName(user.getLogin());
                log.debug("Имя для отображения пустое — в таком случае будет использован логин");
            }
            userStorage.create(user);
            log.debug("Пользователь с логином {} успешно создан", user.getLogin());
        }
        return user;
    }

    /**
     * Метод (эндпоинт) обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    public User update(User user) {
        if (!userStorage.getUsers().containsKey(user.getId())) {
            log.warn("Пользователя с указанным ID {} - не существует", user.getId());
            throw new UserUnknownException("Пользователь с ID " + user.getId() + " не существует");
        }
        if (isValid(user)) {
            userStorage.update(user);
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }
        return user;
    }

    /**
     * Метод проверки валидации пользователя
     *
     * @param user Принятый объект по эндпоинту
     * @return Возвращаем true/false при прохождении валидации
     */
    private boolean isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Электронная почта пользователя пустая или не содержат символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Логин пустой или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения пользователя превышает текущую дату");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            return true;
        }
    }
}
