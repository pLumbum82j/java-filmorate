package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
public class FilmTestDbStorage {
    private final FilmDbService filmDbService;

    @Test
    public void shouldCreateFilm() {
        Film testFilm = Film.builder().id(1L)
                .name("Titanic4")
                .description("Test description4")
                .duration(90)
                .releaseDate(LocalDate.of(1997, 1, 27))
                .mpa(new Mpa(1, "G"))
                .build();
        Film filmActual = filmDbService.create(testFilm);

        assertEquals(filmActual.getId(), testFilm.getId());
        assertEquals(filmActual.getName(), testFilm.getName());
        assertEquals(filmActual.getDescription(), testFilm.getDescription());
        assertEquals(filmActual.getDescription(), testFilm.getDescription());
    }

    @Test
    public void testFindUserById() {
        Film testFilm = Film.builder()
                .name("Titanic4")
                .description("Test description4")
                .duration(90)
                .releaseDate(LocalDate.of(1997, 1, 27))
                .mpa(new Mpa(1, "G"))
                .build();
        Film filmActual = filmDbService.create(testFilm);
        Film get = filmDbService.findFilmById(1L);

        assertEquals(1L, get.getId());
//        assertEquals(filmActual.getName(), get.getName());
//        assertEquals(filmActual.getDescription(), get.getDescription());
//        assertEquals(filmActual.getDescription(), get.getDescription());

    }

    @Test
    void shouldReturnNull_IfFilmIsNotExists() {
        assertThat(filmDbService.findFilmById(1L)).isNull();
    }


}
