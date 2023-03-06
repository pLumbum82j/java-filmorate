package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.UserDbService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class FilmTestDbStorage {
    private final FilmDbService filmDbService;
    private final UserDbService userDbService;
    Film testFilm = Film.builder()
            .id(1L)
            .name("Интерстеллар")
            .description("Следующий шаг человечества станет величайшим")
            .duration(169)
            .releaseDate(LocalDate.of(2014, 05, 21))
            .mpa(new Mpa(1, "G"))
            .genres(List.of(new Genre(1, "Комедия")))
            .build();
    User testUser = User.builder()
            .id(1L)
            .email("dino@yandex.ru")
            .login("Dinozavrr")
            .name("Pavel")
            .birthday(LocalDate.of(2010, 02, 22))
            .build();


    @Test
    public void shouldCreateFilm() {
        Film resultFilm = filmDbService.create(testFilm);

        assertEquals(resultFilm.getId(), testFilm.getId());
        assertEquals(resultFilm.getName(), testFilm.getName());
        assertEquals(resultFilm.getDescription(), testFilm.getDescription());
        assertEquals(resultFilm.getDuration(), testFilm.getDuration());
        assertEquals(resultFilm.getReleaseDate(), testFilm.getReleaseDate());
        assertEquals(resultFilm.getGenres(), testFilm.getGenres());
    }

    @Test
    public void shouldFindFilmById() {
        Film resultFilm = filmDbService.create(testFilm);

        assertEquals(resultFilm.getId(), testFilm.getId());
        assertEquals(resultFilm.getName(), testFilm.getName());
        assertEquals(resultFilm.getDescription(), testFilm.getDescription());
        assertEquals(resultFilm.getDuration(), testFilm.getDuration());
        assertEquals(resultFilm.getReleaseDate(), testFilm.getReleaseDate());
        assertEquals(resultFilm.getMpa(), testFilm.getMpa());


    }

    @Test
    public void shouldAddLike() {
        Film film = filmDbService.create(testFilm);
        userDbService.create(testUser);

        Film resultFilm = filmDbService.addLike(testFilm.getId(), testUser.getId());

        assertEquals(film.getLikeAmout() + 1, resultFilm.getLikeAmout());
    }

    @Test
    public void shouldGetListPopularFilms() {
        Film testFilm2 = Film.builder()
                .id(2L)
                .name("Бронтозавр")
                .description("Шагаем на пути своя")
                .duration(202)
                .releaseDate(LocalDate.of(2014, 05, 21))
                .mpa(new Mpa(1, "G"))
                .likeAmout(0L)
                .build();
        filmDbService.create(testFilm);
        filmDbService.create(testFilm2);
        userDbService.create(testUser);
        Film check = filmDbService.addLike(testFilm2.getId(), testUser.getId());

        List<Film> resultList = filmDbService.getPopularFilms(2, "desc");

        assertEquals(check, resultList.get(0));


    }

    @Test
    public void shouldDeleteLike() {
        Film film = filmDbService.create(testFilm);
        userDbService.create(testUser);
        filmDbService.addLike(testFilm.getId(), testUser.getId());

        Film resultFilm = filmDbService.deleteLike(testFilm.getId(), testUser.getId());

        assertEquals(film.getLikeAmout(), resultFilm.getLikeAmout());
    }

    @Test
    public void shouldUpdateFilm() {
        filmDbService.create(testFilm);
        testFilm.setName("АВАТАР");
        testFilm.setDuration(111);

        Film resultFilm = filmDbService.update(testFilm);

        assertEquals(testFilm.getName(), resultFilm.getName());
        assertEquals(testFilm.getDuration(), resultFilm.getDuration());
    }
}
