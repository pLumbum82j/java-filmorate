package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDbService {

    private final UserStorage userStorage;

    @Autowired
    public UserDbService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    /**
     * Метод получения списка пользователей
     *
     * @return Список пользователей
     */
    public List<User> getUsers() {
        log.debug("Получен запрос на список пользователей");
        return new ArrayList<>(userStorage.getUsers().values());
    }

    /**
     * Метод создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    public User create(User user) {
        User createUser = null;
        if (isValid(user)) {
            if ((user.getName() == null) || (user.getName().isBlank())) {
                user.setName(user.getLogin());
                log.debug("Имя для отображения пустое — в таком случае будет использован логин");
            }
            createUser = userStorage.create(user);
            log.debug("Пользователь с логином {} успешно создан", user.getLogin());
        }
        return createUser;
    }

    /**
     * Метод получения пользователя по id
     *
     * @param id id пользователя
     * @return объект пользователя
     */
    public User findUserById(Long id) {
        User findUser;
        if ((findUser = userStorage.findUserById(id)) == null){
            throw new UserUnknownException("Пользователь с ID " + id + " не существует");
        }
        log.debug("Получен запрос на поиск пользователя {}", id);
        return findUser;
    }

    /**
     * Метод обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    public User update(User user) {
        User updateUser = null;
        userStorage.findUserById(user.getId());
        if (isValid(user)) {
             if ((updateUser = userStorage.update(user)) == null){
                 throw new UserUnknownException("Пользователь с ID " + user.getId() + " не существует");
            }
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }
        return updateUser;
    }

    /**
     * Метод проверки валидации пользователя
     *
     * @param user Принятый объект по эндпоинту
     * @return Возвращаем true/false при прохождении валидации
     */
    private boolean isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            return true;
        }
    }

}
