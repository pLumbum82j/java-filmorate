package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDbService implements UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Autowired
    public UserDbService(@Qualifier("userDbStorage") UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    @Override
    public List<User> getUsers() {
        log.debug("Получен запрос на список пользователей");
        return new ArrayList<>(userStorage.getUsers().values());
    }

    @Override
    public User findUserById(Long id) {
        isParameterCheck(id);
        User findUser;
        if ((findUser = userStorage.findUserById(id)) == null) {
            throw new UserUnknownException("Пользователь с ID " + id + " не существует");
        }
        log.debug("Получен запрос на поиск пользователя {}", id);
        return findUser;
    }

    @Override
    public List<User> getUserFriends(Long id) {
        isParameterCheck(id);
        log.debug("Получен запрос на список друзей пользователя {}", id);
        return friendStorage.getUserFriends(id);
    }

    @Override
    public List<User> getListOfCommonFriends(long firstId, long secondId) {
        isParameterCheck(firstId);
        isParameterCheck(secondId);
        log.debug("Получен запрос на список общих друзей пользователей с ID {} и ID {}", firstId, secondId);
        return friendStorage.getListOfCommonFriends(firstId, secondId);
    }

    @Override
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

    @Override
    public User update(User user) {
        User updateUser = null;
        userStorage.findUserById(user.getId());
        if (isValid(user)) {
            if ((updateUser = userStorage.update(user)) == null) {
                throw new UserUnknownException("Пользователь с ID " + user.getId() + " не существует");
            }
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }
        return updateUser;
    }

    @Override
    public void addFriend(Long firstId, Long secondId) {
        isParameterCheck(firstId);
        isParameterCheck(secondId);
        friendStorage.addFriends(firstId, secondId);
        log.debug("Теперь пользователь ID {} является другом пользователя ID {}", firstId, secondId);

    }

    @Override
    public void deleteFriend(Long firstId, Long secondId) {
        isParameterCheck(firstId);
        isParameterCheck(secondId);
        friendStorage.deleteFriends(firstId, secondId);
        log.debug("Теперь пользователь ID {} не является другом пользователя ID {}", firstId, secondId);
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

    /**
     * Метод проверки входных параметров id на отрицательное значение
     *
     * @param id Входной параметр id
     */
    public void isParameterCheck(Long id) {
        if (id < 0) {
            log.debug("Пользователь с отрицательным id {} не может существовать.", id);
            throw new UserUnknownException("Пользователь с отрицательным id " + id + " не может существовать.");

        }
    }
}
