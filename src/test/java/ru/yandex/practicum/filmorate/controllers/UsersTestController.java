package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersTestController {


    User testUser;
    UserController userController = new UserController();

    @BeforeEach
    public void beforeEach() {
        testUser = User.builder()
                .email("dinozavr@yandex.ru")
                .login("Dino")
                .name("Pasha")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();

    }

    @Test
    public void shouldReturnListOfUsers() {
        userController.create(testUser);

        assertTrue(userController.findAll().contains(testUser));

    }

    @Test
    public void shouldThrowExceptionIfMailIsEmpty() {
        testUser.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMailIsNull() {
        testUser.setEmail(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfMailNotContainDog() {
        testUser.setEmail("mail");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfLoginIsEmpty() {
        testUser.setLogin("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfLoginIsNull() {
        testUser.setLogin(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfLoginContainsSpaces() {
        testUser.setLogin("Meg Zavr");

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());

    }

    @Test
    public void shouldThrowExceptionIfTheBirthdayIsInTheFuture() {
        testUser.setBirthday(LocalDate.of(3000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.create(testUser));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    public void shouldChangeTheNameIfItIsEmpty() {
        testUser.setName("");

        User user = userController.create(testUser);

        assertEquals(user.getName(), testUser.getLogin());
    }

    @Test
    public void shouldThrowExceptionIfIdIsDoesNotExist() {
        userController.create(testUser);

        User testUser2 = User.builder()
                .id(2L)
                .email("zoozaver@yandex.ru")
                .login("Rex")
                .name("Olga")
                .birthday(LocalDate.of(2012, 12, 12))
                .build();
        UserUnknownException exception = assertThrows(UserUnknownException.class, () -> userController.update(testUser2));

        assertEquals("Пользователь с ID " + testUser2.getId() + " не существует", exception.getMessage());
    }

    @Test
    public void shouldSuccessfullyChangeUser() {
        userController.create(testUser);

        User testUser2 = User.builder()
                .id(1L)
                .email("zoozaver@yandex.ru")
                .login("Rex")
                .name("Olga")
                .birthday(LocalDate.of(2012, 12, 12))
                .build();
        User userValue = userController.update(testUser2);

        assertEquals(userValue, testUser2);
    }
}
