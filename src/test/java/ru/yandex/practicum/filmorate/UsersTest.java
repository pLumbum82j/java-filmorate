package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsersTest {
    User testUser;
    @BeforeEach
    public void beforeEach() {
        testUser = User.builder()
                .email("mirasolar@mail.ru")
                .login("Mira-Mira")
                .name("Mira")
                .birthday(LocalDate.of(2000,12,12))
                .build();
    }

    @Test
    public void getListOfUsers() throws ValidationException {
        List<User> testFilmsArray = new ArrayList<>();
        assertEquals(controller.getUsers(), testFilmsArray);

        controller.createUser(testUser);
        testFilmsArray.add(testUser);
        assertEquals(controller.getUsers(), testFilmsArray);
    }
}
