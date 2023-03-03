package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserIsAlreadyFriendException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.UsersAreNotFriendsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
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
     * Метод получения пользователя по id
     *
     * @param id id пользователя
     * @return объект пользователя
     */
    public User findUserById(Long id) {
        containUserId(id);
        log.debug("Получен запрос на поиск пользователя {}", id);
        return userStorage.findUserById(id);
    }

    /**
     * Метод получения списка друзей пользователя по id
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    public List<User> getUserFriends(Long id) {
        containUserId(id);
        log.debug("Получен запрос на список друзей пользователя {}", id);
        return userStorage.findUserById(id)
                .getFriendsList()
                .stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    /**
     * Метод (эндпоинт) получения списка общих друзей двух пользователей по id
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     * @return список общих друзей двух пользователей
     */
    public List<User> getListOfCommonFriends(long firstId, long secondId) {
        containUserId(firstId);
        containUserId(secondId);
        log.debug("Получен запрос на список общих друзей пользователей с ID {} и ID {}", firstId, secondId);
        List<User> friendsFirstUser = userStorage.findUserById(firstId)
                .getFriendsList()
                .stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
        List<User> friendsSecondUser = userStorage.findUserById(secondId)
                .getFriendsList()
                .stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());

        return friendsFirstUser.stream().filter(friendsSecondUser::contains).collect(Collectors.toList());
    }

    /**
     * Метод создания пользователя
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
     * Метод обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    public User update(User user) {
        containUserId(user.getId());
        if (isValid(user)) {
            userStorage.update(user);
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }
        return user;
    }

    /**
     * Метод добавления пользователя в друзья
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    public void addFriend(Long firstId, Long secondId) {
        containUserId(firstId);
        containUserId(secondId);
        if (isFriendshipCheck(firstId, secondId)) {
            throw new UserIsAlreadyFriendException("Пользователь с ID" + firstId + " уже является другом пользователя ID" + secondId);
        }
        userStorage.findUserById(firstId).getFriendsList().add(secondId);
        userStorage.findUserById(secondId).getFriendsList().add(firstId);
        userStorage.update(userStorage.findUserById(firstId));
        userStorage.update(userStorage.findUserById(secondId));
        log.debug("Теперь пользователь ID {} является другом пользователя ID {}", firstId, secondId);

    }

    /**
     * Метод удаления пользователя из друзей
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     */
    public void deleteFriend(Long firstId, Long secondId) {
        containUserId(firstId);
        containUserId(secondId);
        if (!isFriendshipCheck(firstId, secondId)) {
            throw new UsersAreNotFriendsException("Пользователь с ID" + firstId + " не является другом пользователя ID" + secondId);
        }
        userStorage.findUserById(firstId).getFriendsList().remove(secondId);
        userStorage.findUserById(secondId).getFriendsList().remove(firstId);
        userStorage.update(userStorage.findUserById(firstId));
        userStorage.update(userStorage.findUserById(secondId));
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
     * Метод проверки присутсивя пользователя на сервере
     *
     * @param id id пользователя
     */
    private void containUserId(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new UserUnknownException("Пользователь с ID " + id + " не существует");
        }
    }

    /**
     * Метод проверки дружбы между двумя пользователями
     *
     * @param firstId  id первого пользователя
     * @param secondId id второго пользователя
     * @return Возвращаем true/false при прохождении валидации
     */
    private boolean isFriendshipCheck(Long firstId, Long secondId) {
        return userStorage.getUsers().get(firstId).getFriendsList().contains(secondId);
    }
}
