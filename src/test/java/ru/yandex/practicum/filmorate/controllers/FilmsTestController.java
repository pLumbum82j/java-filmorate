package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmsTestController {
    private Validator validator;
    String messageException;
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

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    public void readException() {
        Set<ConstraintViolation<Film>> violations = validator.validate(testFilm);
        for (ConstraintViolation<Film> viol : violations) {
            messageException = viol.getMessage();
        }
    }

    @Test
    public void shouldReturnListOfUsers() {
        filmController.create(testFilm);

        assertTrue(filmController.findAll().contains(testFilm));
    }

    @Test
    public void shouldThrowExceptionIfNameIsEmptyThroughAnnotations() {
        testFilm.setName("");

        readException();

        assertEquals(messageException, "Название фильма не может быть пустым");
    }

    @Test
    public void shouldThrowExceptionIfNameIsNullThroughAnnotations() {
        testFilm.setName(null);

        readException();

        assertEquals(messageException, "Название фильма не может быть пустым");
    }


    @Test
    public void shouldThrowExceptionIfDescriptionIsMoreThan200CharactersThroughAnnotations() {
        testFilm.setDescription("TRACE (англ. «отслеживать») — для трассировочных сообщений, которые предоставляют очень" +
                " подробную информацию о каком-либо процессе. Сама запись при этом может и не содержать большого объёма " +
                "данных. Подробность достигается за счёт количества записей. На этом уровне может записываться всё, что " +
                "происходит в системе: выбор ветки в условии if — else, значения параметров внутри циклов, каждый шаг " +
                "алгоритма и так далее.");

        readException();

        assertEquals(messageException, "Максимальная длина описания более 200 символов");
    }

    @Test
    public void shouldThrowExceptionIfTheReleaseDateIsInTheLongPastThroughAnnotations() {
        testFilm.setReleaseDate(LocalDate.of(1890, 1, 1));

        readException();

        assertEquals(messageException, "Дата релиза должна быть позже 1895-12-28");
    }

    @Test
    public void shouldThrowExceptionIfDurationIsNegativeThroughAnnotations() {
        testFilm.setDuration(-1L);

        readException();

        assertEquals(messageException, "Продолжительность фильма нулевая или отрицательная");
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

