package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.UserIsAlreadyFriendException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.UsersAreNotFriendsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsersTestController {


    User testUser;
    User testUser2;
    UserController userController;


    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
        testUser = User.builder()
                .email("dinozavr@yandex.ru")
                .login("Dino")
                .name("Pasha")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        testUser2 = User.builder()
                .id(2L)
                .email("zoozaver@yandex.ru")
                .login("Rex")
                .name("Olga")
                .birthday(LocalDate.of(2012, 12, 12))
                .build();

    }

    @Test
    public void shouldReturnListOfUsers() {
        userController.create(testUser);

        assertTrue(userController.getUsers().contains(testUser));

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

        UserUnknownException exception = assertThrows(UserUnknownException.class, () -> userController.update(testUser2));

        assertEquals("Пользователь с ID " + testUser2.getId() + " не существует", exception.getMessage());
    }

    @Test
    public void shouldSuccessfullyChangeUser() {
        userController.create(testUser);
        userController.create(testUser2);

        User userValue = userController.update(testUser2);

        assertEquals(userValue, testUser2);
    }

    @Test
    public void shouldReturnUserById() {
        User testUserTemp = testUser;

        UserUnknownException exception = assertThrows(UserUnknownException.class, () -> userController.getUsersById(1L));

        assertEquals("Пользователь с ID 1 не существует", exception.getMessage());

        userController.create(testUser);

        assertEquals(testUserTemp, userController.getUsersById(testUser.getId()));
    }

    @Test
    public void shouldAddFriend() {
        userController.create(testUser);
        userController.create(testUser2);
        Set<Long> testList = new HashSet<>();

        assertEquals(testList, testUser.getFriendsList());
        assertEquals(testList, testUser2.getFriendsList());

        userController.addFriend(testUser.getId(), testUser2.getId());
        testList.add(testUser2.getId());

        assertEquals(testList, testUser.getFriendsList());

        UserIsAlreadyFriendException exception = assertThrows(UserIsAlreadyFriendException.class, () -> userController.addFriend(testUser.getId(), testUser2.getId()));

        assertEquals("Пользователь с ID" + testUser.getId() + " уже является другом пользователя ID" + testUser2.getId(), exception.getMessage());

        testList.remove(testUser2.getId());
        testList.add(testUser.getId());

        assertEquals(testList, testUser2.getFriendsList());
    }

    @Test
    public void shouldDeleteFriend() {
        userController.create(testUser);
        userController.create(testUser2);
        Set<Long> testList = new HashSet<>();

        UsersAreNotFriendsException exception = assertThrows(UsersAreNotFriendsException.class, () -> userController.deleteFriend(testUser.getId(), testUser2.getId()));

        assertEquals("Пользователь с ID" + testUser.getId() + " не является другом пользователя ID" + testUser2.getId(), exception.getMessage());

        userController.addFriend(testUser.getId(), testUser2.getId());
        testList.add(testUser2.getId());

        assertEquals(testList, testUser.getFriendsList());

        testList.remove(testUser2.getId());
        userController.deleteFriend(testUser.getId(), testUser2.getId());

        assertEquals(testList, testUser.getFriendsList());
        assertEquals(testList, testUser2.getFriendsList());
    }

    @Test
    public void shouldReturnListOfUsersWhoAreFriends() {
        userController.create(testUser);
        userController.create(testUser2);
        List<User> testList = new ArrayList<>();

        assertEquals(testList, userController.getUserFriends(testUser.getId()));

        userController.addFriend(testUser.getId(), testUser2.getId());
        testList.add(testUser2);

        assertEquals(testList, userController.getUserFriends(testUser.getId()));
    }

    @Test
    public void shouldReturnListOfUsersWhoAreMutualFriends() {
        User testUser3 = User.builder()
                .id(2L)
                .email("zoozaver@yandex.ru")
                .login("Rex")
                .name("Olga")
                .birthday(LocalDate.of(2012, 12, 12))
                .build();
        userController.create(testUser);
        userController.create(testUser2);
        userController.create(testUser3);
        List<User> testList = new ArrayList<>();

        assertEquals(testList, userController.getListOfCommonFriends(testUser.getId(), testUser2.getId()));

        userController.addFriend(testUser.getId(), testUser3.getId());
        userController.addFriend(testUser2.getId(), testUser3.getId());
        testList.add(testUser3);

        assertEquals(testList, userController.getListOfCommonFriends(testUser.getId(), testUser2.getId()));
    }


}
