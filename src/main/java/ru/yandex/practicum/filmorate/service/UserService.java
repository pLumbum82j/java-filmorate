package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;
import java.util.stream.Collectors;

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

    public User findUserById(Long id) {
        isContainUserId(id);
        log.debug("Получен запрос на поиск пользователя {}", id);
        return userStorage.findUserById(id);
    }

    public List<User> getUserFriends(Long id) {
        isContainUserId(id);
        log.debug("Получен запрос на список друзей пользователя {}", id);
        return userStorage.findUserById(id)
                .getFriendsList()
                .stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(long firstId, long secondId) {
        isContainUserId(firstId);
        isContainUserId(secondId);
//        if (!isFriendshipCheck(firstId, secondId)) {
//            log.debug("Пользователь с ID {} не является другом пользователя ID {}", firstId, secondId);
//            throw new UsersAreNotFriendsException("Пользователи не являются друзьями");
//        }
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
        isContainUserId(user.getId());
        if (isValid(user)) {
            userStorage.update(user);
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }
        return user;
    }

    public void addFriend(Long firstId, Long secondId) {
        isContainUserId(firstId);
        isContainUserId(secondId);
        if (isFriendshipCheck(firstId, secondId)) {
            log.debug("Пользователь с ID {} уже является другом пользователя ID {}", firstId, secondId);
            throw new UserIsAlreadyFriendException("Пользователи уже являются друзьями");
        }
        userStorage.findUserById(firstId).getFriendsList().add(secondId);
        userStorage.findUserById(secondId).getFriendsList().add(firstId);
        userStorage.update(userStorage.findUserById(firstId));
        userStorage.update(userStorage.findUserById(secondId));
        log.debug("Теперь пользователь ID {} является другом пользователя ID {}", firstId, secondId);

    }

    public void deleteFriend(Long firstId, Long secondId) {
        isContainUserId(firstId);
        isContainUserId(secondId);
        if (!isFriendshipCheck(firstId, secondId)) {
            log.debug("Пользователь с ID {} не является другом пользователя ID {}", firstId, secondId);
            throw new UsersAreNotFriendsException("Пользователи не являются друзьями");
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

    private void isContainUserId(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            log.warn("Пользователя с указанным ID {} - не существует", id);
            throw new UserUnknownException("Пользователь с ID " + id + " не существует");
        }
    }

    private boolean isFriendshipCheck(Long firstId, Long secondId) {
        return userStorage.getUsers().get(firstId).getFriendsList().contains(secondId);
    }

}
