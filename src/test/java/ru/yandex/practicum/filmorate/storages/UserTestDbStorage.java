package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class UserTestDbStorage {
    private final UserDbService userDbService;

    User testUser = User.builder()
            .id(1L)
            .email("dino@yandex.ru")
            .login("Dinozavrr")
            .name("Pavel")
            .birthday(LocalDate.of(2010, 02, 22))
            .build();

    @Test
    public void shouldCreateUser() {
        User resultUser = userDbService.create(testUser);

        assertEquals(resultUser.getId(), testUser.getId());
        assertEquals(resultUser.getEmail(), testUser.getEmail());
        assertEquals(resultUser.getName(), testUser.getName());
        assertEquals(resultUser.getLogin(), testUser.getLogin());
        assertEquals(resultUser.getBirthday(), testUser.getBirthday());
    }

    @Test
    public void shouldFindUserById() {
        userDbService.create(testUser);

        User resultUser = userDbService.findUserById(1L);

        assertEquals(resultUser.getId(), testUser.getId());
        assertEquals(resultUser.getEmail(), testUser.getEmail());
        assertEquals(resultUser.getName(), testUser.getName());
        assertEquals(resultUser.getLogin(), testUser.getLogin());
        assertEquals(resultUser.getBirthday(), testUser.getBirthday());
    }

    @Test
    public void shouldUpdateUser() {
        userDbService.create(testUser);
        testUser.setName("Григорий");
        testUser.setLogin("Пуля");

        User resultUser = userDbService.update(testUser);

        assertEquals(resultUser.getName(), testUser.getName());
        assertEquals(resultUser.getLogin(), testUser.getLogin());
    }

    @Test
    public void shouldGetListUsers() {
        userDbService.create(testUser);
        List<User> checkList = new ArrayList<>();
        checkList.add(testUser);

        List<User> resultList = userDbService.getUsers();

        assertEquals(resultList, checkList);
        assertEquals(resultList.size(),checkList.size());
    }

    @Test
    public void shouldAddFriendToUser(){
        User testUser2 = User.builder()
                .id(2L)
                .email("zoozavvr@yandex.ru")
                .login("Tirexxx")
                .name("Olga")
                .birthday(LocalDate.of(2011, 1, 2))
                .build();
        userDbService.create(testUser);
        userDbService.create(testUser2);

        userDbService.addFriend(testUser.getId(), testUser2.getId());
        List<User> resultUser = userDbService.getUserFriends(testUser.getId());

        assertEquals(resultUser.get(0), testUser2);
        assertEquals(resultUser.size(), 1);
    }

    @Test
    public void shouldDeleteFriendToUser(){
        User testUser2 = User.builder()
                .id(2L)
                .email("zoozavvr@yandex.ru")
                .login("Tirexxx")
                .name("Olga")
                .birthday(LocalDate.of(2011, 1, 2))
                .build();
        userDbService.create(testUser);
        userDbService.create(testUser2);
        userDbService.addFriend(testUser.getId(), testUser2.getId());

        userDbService.deleteFriend(testUser.getId(), testUser2.getId());
        List<User> resultUser = userDbService.getUserFriends(testUser.getId());

        assertEquals(resultUser.size(), 0);
    }

    @Test
    public void shouldGetListOfCommonFriends(){
        User testUser2 = User.builder()
                .id(2L)
                .email("zoozavvr@yandex.ru")
                .login("Tirexxx")
                .name("Olga")
                .birthday(LocalDate.of(2011, 1, 2))
                .build();
        User testUser3 = User.builder()
                .id(3L)
                .email("parus@yandex.ru")
                .login("Totoshka")
                .name("Sergey")
                .birthday(LocalDate.of(2001, 5, 5))
                .build();
        userDbService.create(testUser);
        userDbService.create(testUser2);
        userDbService.create(testUser3);
        userDbService.addFriend(testUser.getId(), testUser3.getId());
        userDbService.addFriend(testUser2.getId(), testUser3.getId());

        List<User> resultList = userDbService.getListOfCommonFriends(testUser.getId(), testUser2.getId());

        assertEquals(resultList.size(), 1);
        assertEquals(resultList.get(0), testUser3);
    }

}
