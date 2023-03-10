package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

/**
 * Класс Контроллер по энпоинту Users
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(@Qualifier("userDbService") UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод (эндпоинт) получения списка пользователей
     *
     * @return Список пользователей
     */
    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Метод (эндпоинт) получения пользователя по id
     *
     * @param id id пользователя
     * @return объект пользователя
     */
    @GetMapping("/{id}")
    public User getUsersById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    /**
     * Метод (эндпоинт) получения списка друзей пользователя по id
     *
     * @param id id пользователя
     * @return список друзей пользователя
     */
    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Long id) {
        return userService.getUserFriends(id);
    }

    /**
     * Метод (эндпоинт) получения списка общих друзей двух пользователей по id
     *
     * @param id      id первого пользователя
     * @param otherId id второго пользователя
     * @return список общих друзей двух пользователей
     */
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getListOfCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getListOfCommonFriends(id, otherId);
    }

    /**
     * Метод (эндпоинт) создания пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return созданный объект пользователя
     */
    @PostMapping()
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    /**
     * Метод (эндпоинт) обновления пользователя
     *
     * @param user Принятый объект пользователя по эндпоинту
     * @return изменённый объект пользователя
     */
    @PutMapping()
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    /**
     * Метод (эндпоинт) добавления пользователя в друзья
     *
     * @param id       id первого пользователя
     * @param friendId id второго пользователя
     */
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    /**
     * Метод (эндпоинт) удаления пользователя из друзей
     *
     * @param id       id первого пользователя
     * @param friendId id второго пользователя
     */
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }
}
