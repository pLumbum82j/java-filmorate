package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UpdateFilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.SORTS;

@Slf4j
@Service
public class FilmDbService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;

    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    @Override
    public List<Film> getPopularFilms(Integer count, String sort) {
        if (!SORTS.contains(sort)) {
            throw new IncorrectParameterException("Некорректное значение sort, введите: asc или desc");
        }
        if (count <= 0) {
            throw new IncorrectParameterException("Значение count не может быть меньше 1");
        }
        log.debug("Получен запрос на список из {} популярных фильмов", count);
        return filmStorage.getPopularFilms(count, sort);
    }

    @Override
    public Film findFilmById(Long id) {
        Film findFilm;
        if ((findFilm = filmStorage.findFilmById(id)) == null) {
            throw new FilmUnknownException("Фильм с ID " + id + " не существует");
        }
        return findFilm;
    }

    @Override
    public Film create(Film film) {
        Film resultFilm = filmStorage.create(film);
        log.debug("Фильм {} создан", film.getName());
        return resultFilm;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        isParameterCheck(filmId, userId);
        log.debug("Получен запрос на добавления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        likeStorage.addLike(filmId, userId);
        return filmStorage.findFilmById(filmId);
    }

    @Override
    public Film update(Film film) {
        if (filmStorage.findFilmById(film.getId()) != null) {
            log.debug("Получен запрос на обновление Фильма с ID " + film.getId());
            Film updateFilm = filmStorage.update(film);
            return updateFilm;
        } else {
            throw new UpdateFilmUnknownException("Фильм с ID " + film.getId() + " не найден");
        }
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        isParameterCheck(filmId, userId);
        log.debug("Получен запрос на удаление Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        likeStorage.deleteLike(filmId, userId);
        return filmStorage.findFilmById(filmId);
    }

    /**
     * Метод проверки входных параметров filmId и userId на отрицательное значение
     *
     * @param filmId Входной параметр filmId
     * @param userId Входной параметр userId
     */
    private void isParameterCheck(long filmId, long userId) {
        if (filmId < 0) {
            log.debug("Фильм с отрицательным id {} не может существовать.", filmId);
            throw new FilmUnknownException("Фильм с ID " + filmId + " не может ровняться нулю или быть отрицательным");
        }
        if (userId < 0) {
            log.debug("Пользователь с отрицательным id {} не может существовать.", userId);
            throw new UserUnknownException("Фильм с ID " + userId + " не может ровняться нулю или быть отрицательным");
        }
    }

}


