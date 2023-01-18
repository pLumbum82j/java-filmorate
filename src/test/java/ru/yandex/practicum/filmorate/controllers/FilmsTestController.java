package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmsTestController {

    Film testFilm;
    FilmController filmController = new FilmController();

    @BeforeEach
    public void beforeEach() {
        testFilm = Film.builder()
                .name("Парк Периода Яндекс")
                .description("Прохождение очень сложных основ Java в тропических джунглях")
                .releaseDate(LocalDate.of(2022, 7, 27))
                .duration(40L)
                .build();

    }

    @Test
    public void shouldReturnListOfUsers() {
        filmController.create(testFilm);

        assertTrue(filmController.findAll().contains(testFilm));
    }

    @Test
    public void shouldThrowExceptionIfNameIsEmpty() {
        testFilm.setName("");

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));
        assertEquals("Поле с названием фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfNameIsNull() {
        testFilm.setName(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));
        assertEquals("Поле с названием фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfDescriptionIsMoreThan200Characters() {
        testFilm.setDescription("TRACE (англ. «отслеживать») — для трассировочных сообщений, которые предоставляют очень" +
                " подробную информацию о каком-либо процессе. Сама запись при этом может и не содержать большого объёма " +
                "данных. Подробность достигается за счёт количества записей. На этом уровне может записываться всё, что " +
                "происходит в системе: выбор ветки в условии if — else, значения параметров внутри циклов, каждый шаг " +
                "алгоритма и так далее.");

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));
        assertEquals("Максимальная длина описания фильма имеет более 200 символов", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfTheReleaseDateIsInTheLongPast() {
        testFilm.setReleaseDate(LocalDate.of(1890, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfDurationIsNegative() {
        testFilm.setDuration(-1L);

        ValidationException exception = assertThrows(ValidationException.class, () -> filmController.create(testFilm));
        assertEquals("Продолжительность фильма не может быть отрицательной", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionIfIdIsDoesNotExist() {
        filmController.create(testFilm);
        Film testFilm2 = Film.builder()
                .id(2L)
                .name("Южный парк Яндекса")
                .description("Java в двух словах")
                .releaseDate(LocalDate.of(2021, 1, 27))
                .duration(40L)
                .build();

        FilmUnknownException exception = assertThrows(FilmUnknownException.class, () -> filmController.update(testFilm2));
        assertEquals("Фильм с ID " + testFilm2.getId() + " не существует", exception.getMessage());
    }

    @Test
    public void shouldSuccessfullyChangeFilm() {
        filmController.create(testFilm);
        Film testFilm2 = Film.builder()
                .id(1L)
                .name("Южный парк Яндекса")
                .description("Java в двух словах")
                .releaseDate(LocalDate.of(2021, 1, 27))
                .duration(30L)
                .build();

        Film filmValue = filmController.update(testFilm2);

        assertEquals(filmValue, testFilm2);
    }
}

