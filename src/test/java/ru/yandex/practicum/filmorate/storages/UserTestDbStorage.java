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
    private final FilmDbService filmDbService;
    private final UserDbService userDbService;

    Film testFilm = Film.builder()
            .id(1L)
            .name("Интерстеллар")
            .description("Следующий шаг человечества станет величайшим")
            .duration(169)
            .releaseDate(LocalDate.of(2014, 05, 21))
            .mpa(new Mpa(1, "G"))
            //.genres()
            .build();
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
    public void shouldUpdateFilm() {
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
}
