package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;
import static ru.yandex.practicum.filmorate.Constants.SORTS;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    /**
     * Метод получения списка фильмов
     *
     * @return Список филмьов
     */
    public List<Film> getFilms() {
        log.debug("Получен запрос на список фильмов");
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    /**
     * Метод получения списка популярных фильмов
     *
     * @param count количество фильмов в списке
     * @param sort  сортировка по убыванию/возрастанию like
     * @return Список филмьов
     */
    public List<Film> getPopularFilms(Integer count, String sort) {
        if (!SORTS.contains(sort)) {
            throw new IncorrectParameterException("Некорректное значение sort, введите: asc или desc");
        }
        if (count <= 0) {
            throw new IncorrectParameterException("Значение count не может быть меньше 1");
        }
        log.debug("Получен запрос на список из {} популярных фильмов", count);
        return filmStorage.getFilms()
                .values()
                .stream()
                .sorted((f0, f1) -> compare(f0, f1, sort))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Метод получения фильма по id
     *
     * @param id id фильма
     * @return объект фильма
     */
    public Film findFilmById(Long id) {
        ContainFilmId(id);
        return filmStorage.findFilmById(id);
    }

    /**
     * Метод создания фильма
     *
     * @param film принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    public Film create(Film film) {
        filmStorage.create(film);
        log.debug("Фильм {} создан", film.getName());
        return film;
    }

    /**
     * Метод добавления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    public Film addLike(Long filmId, Long userId) {
        ContainFilmId(filmId);
        if (userStorage.isContainUserId(userId)) {
            log.warn("Пользователя с указанным ID {} - не существует", userId);
            throw new UserUnknownException("Пользователь с ID " + userId + " не существует");
        }
        log.debug("Получен запрос на добавления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        filmStorage.getFilms().get(filmId).getLikes().add(userId);
        return filmStorage.findFilmById(filmId);
    }

    /**
     * Метод обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    public Film update(Film film) {
        ContainFilmId(film.getId());
        filmStorage.update(film);
        log.debug("Фильм {} изменён", film.getName());

        return film;
    }

    /**
     * Метод удаления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    public Film deleteLike(Long filmId, Long userId) {
        ContainFilmId(filmId);
        if (userStorage.isContainUserId(userId)) {
            log.warn("Пользователя с указанным ID {} - не существует", userId);
            throw new UserUnknownException("Пользователь с ID " + userId + " не существует");
        }
        log.debug("Получен запрос на удаления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
        return filmStorage.findFilmById(filmId);
    }

    /**
     * Метод проверки присутсивя фильма на сервере
     *
     * @param id id фильма
     */
    public void ContainFilmId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            log.warn("Фильм с указанным ID {} - не существует", id);
            throw new FilmUnknownException("Фильм с ID " + id + " не существует");
        }
    }

    /**
     * Метод сортировки по убыванию/возрастания
     *
     * @param f0   фильма №1
     * @param f1   фильма №2
     * @param sort сортировка убыванию/возрастания
     * @return отсортированный элемент
     */
    private int compare(Film f0, Film f1, String sort) {
        int result = f0.getLikes().size() - (f1.getLikes().size());
        if (sort.equals(DESCENDING_ORDER)) {
            result = -1 * result;
        }
        return result;
    }

}
