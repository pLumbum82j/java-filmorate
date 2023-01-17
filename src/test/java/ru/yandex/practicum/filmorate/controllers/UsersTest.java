package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UsersTest {
    User testUser;
    UserController userController = new UserController();
    @BeforeEach
    public void beforeEach() {
        testUser = User.builder()
                .email("dinozavr@yandex.ru")
                .login("MegaZavr")
                .name("Pasha")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
    }

    @Test
    public void shouldReturnListOfUsers() {
        User userValue = null;

        userController.create(testUser);
        List<User> userMap = userController.findAll();
        for (User user : userMap) {
            userValue = user;
        }

        assertEquals(userValue, testUser);
    }

    @Test
    public void shouldThrowExceptionIfMailIsEmpty() {
        testUser.setEmail("zaccc");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfLoginIsEmpty() {
        testUser.setLogin("");
        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }
}
