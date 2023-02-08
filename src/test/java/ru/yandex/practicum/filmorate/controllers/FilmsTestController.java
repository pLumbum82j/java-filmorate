package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmsTestController {
    private Validator validator;
    String messageException;
    Film testFilm;
    FilmController filmController;
    User testUser;
    User testUser2;


    @BeforeEach
    public void beforeEach() {
        testUser = User.builder()
                .email("dinozavr@yandex.ru")
                .login("Dino")
                .name("Pasha")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        testUser2 = User.builder()
                .email("zzzzavr@mail.ru")
                .login("Zoozavr")
                .name("Anton")
                .birthday(LocalDate.of(2011, 11, 11))
                .build();
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        userStorage.create(testUser);
        userStorage.create(testUser2);
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), userStorage));
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

        assertTrue(filmController.getFilms().contains(testFilm));
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

    @Test
    public void shouldAddLikeFilm() {
        filmController.create(testFilm);
        Set<Long> testLikes = new HashSet<>();

        assertEquals(testLikes, filmController.getFilmById(testFilm.getId()).getLikes());

        filmController.addLike(testUser.getId(), testFilm.getId());
        testLikes.add(testUser.getId());

        assertEquals(testLikes, filmController.getFilmById(testFilm.getId()).getLikes());
    }

    @Test
    public void shouldDeleteLikeFilm() {
        filmController.create(testFilm);
        Set<Long> testLikes = new HashSet<>();
        testLikes.add(testUser.getId());
        testFilm.getLikes().add(testUser.getId());

        assertEquals(testLikes, filmController.getFilmById(testFilm.getId()).getLikes());

        filmController.deleteLike(testUser.getId(), testFilm.getId());
        testLikes.remove(testUser.getId());

        assertEquals(testLikes, filmController.getFilmById(testFilm.getId()).getLikes());
    }

    @Test
    public void shouldBringBackPopularFilm() {
        Film testFilm2 = Film.builder()
                .name("Яндекс и новые приключения")
                .description("GPT-Chat и его фишки")
                .releaseDate(LocalDate.of(2023, 1, 20))
                .duration(140L)
                .build();
        filmController.create(testFilm);
        filmController.create(testFilm2);
        filmController.addLike(testFilm.getId(), testUser.getId());
        filmController.addLike(testFilm.getId(), testUser2.getId());
        filmController.addLike(testFilm2.getId(), testUser2.getId());
        List<Film> testList = new ArrayList<>();
        testList.add(testFilm);
        testList.add(testFilm2);

        assertEquals(testList, filmController.getPopularFilms(2, "desc"));

        IncorrectParameterException exception = assertThrows(IncorrectParameterException.class, () -> filmController.getPopularFilms(2, "abcd"));

        assertEquals("Некорректное значение sort, введите: asc или desc", exception.getMessage());

        exception = assertThrows(IncorrectParameterException.class, () -> filmController.getPopularFilms(0, "desc"));

        assertEquals("Значение count не может быть меньше 1", exception.getMessage());


    }

}

