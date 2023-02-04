package ru.yandex.practicum.filmorate.controller;

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        return userService.update(user);
    }
}
