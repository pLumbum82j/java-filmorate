package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @GetMapping()
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        if (isValid(user)) {
            if ((user.getName() == null) || (user.getName().isBlank())) {
                user.setName(user.getLogin());
                log.debug("Имя для отображения пустое — в таком случае будет использован логин");
            }
            user.setId(id);
            users.put(id, user);
            log.debug("Пользователь с логином {} успешно создан", user.getLogin());
            id++;
        }
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.debug("Пользователя с указанным ID {} - не существует", user.getId());
            throw new UserUnknownException("Пользователь с ID" + user.getId() + " не существует");
        }
        if (isValid(user)) {
            users.put(user.getId(), user);
            log.debug("Пользователь с логином {} успешно изменён", user.getLogin());
        }

        return user;
    }


    private boolean isValid(User user) {
        if (user.getEmail().isBlank() || user.getEmail() == null || !user.getEmail().contains("@")) {
            log.debug("Электронная почта пользователя пустая или не содержат символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin() == null || user.getLogin().contains(" ")) {
            log.debug("Логин пустой или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
//        } else if (user.getName() == null || user.getName().isEmpty()) {
//            user.setName(user.getLogin());
//            log.debug("Имя для отображения пустое — в таком случае будет использован логин");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug("Дата рождения пользователя превышает текущую дату");
            throw new ValidationException("Дата рождения не может быть в будущем.");
        } else {
            return true;
        }
    }
}
