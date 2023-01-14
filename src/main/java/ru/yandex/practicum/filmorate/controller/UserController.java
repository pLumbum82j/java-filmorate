package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private long id = 1;

    @GetMapping()
    public Map<Integer, User> findAll() {
        return users;
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        user.setId(id);
        users.put((int) id, user);
        id++;
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        users.put((int) user.getId(), user);
        return user;
    }
}
