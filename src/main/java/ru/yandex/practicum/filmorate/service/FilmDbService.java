package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaUnknownException;
import ru.yandex.practicum.filmorate.exception.UpdateFilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmDbService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmDbService(@Qualifier("filmDbStorage") FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    public Film create(Film film) throws SQLException {
        Film resultFilm = filmStorage.create(film);
        log.debug("Фильм {} создан", film.getName());
        return resultFilm;
    }
    public Film update(Film film) throws  SQLException {
        if (filmStorage.findFilmById(film.getId()) != null) {
            log.debug("Получен запрос на обновление Фильма с ID " + film.getId());
            Film updateFilm = filmStorage.update(film);
            return updateFilm;
        } else {
            throw new UpdateFilmUnknownException("Фильм с ID " + film.getId() + " не найден");
        }
    }
    public Film addLike(Long filmId, Long userId) {
        if (filmStorage.findFilmById(filmId) == null) {
            throw new UpdateFilmUnknownException("Фильм с ID " + filmId + " не найден");
        }
        if (userStorage.isContainUserId(userId)) {
            throw new UserUnknownException("Пользователь с ID " + userId + " не существует");
        }
        log.debug("Получен запрос на добавления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        filmStorage.addLike(filmId, userId);
        return filmStorage.findFilmById(filmId);
    }


}


