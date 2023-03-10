package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmInMemoryService implements FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmInMemoryService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                               @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getFilms() {
        log.debug("Получен запрос на список фильмов");
        return new ArrayList<>(filmStorage.getFilms().values());
    }

    @Override
    public List<Film> getPopularFilms(Integer count, String sort) {
        log.debug("Получен запрос на список из {} популярных фильмов", count);
        return filmStorage.getPopularFilms(count, sort);
    }

    @Override
    public Film findFilmById(Long id) {
        containFilmId(id);
        return filmStorage.findFilmById(id);
    }

    @Override
    public Film create(Film film) {
        filmStorage.create(film);
        log.debug("Фильм {} создан", film.getName());
        return film;
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        containFilmId(filmId);
        if (userStorage.isContainUserId(userId)) {
            throw new UserUnknownException("Пользователь с ID " + userId + " не существует");
        }
        log.debug("Получен запрос на добавления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        filmStorage.getFilms().get(filmId).getLikes().add(userId);
        return filmStorage.findFilmById(filmId);
    }

    @Override
    public Film update(Film film) {
        containFilmId(film.getId());
        filmStorage.update(film);
        log.debug("Фильм {} изменён", film.getName());

        return film;
    }

    @Override
    public Film deleteLike(Long filmId, Long userId) {
        containFilmId(filmId);
        if (userStorage.isContainUserId(userId)) {
            throw new UserUnknownException("Пользователь с ID " + userId + " не существует");
        }
        log.debug("Получен запрос на удаления Like пользователя с ID {} в фильм с ID {}", userId, filmId);
        filmStorage.getFilms().get(filmId).getLikes().remove(userId);
        return filmStorage.findFilmById(filmId);
    }

    /**
     * Метод проверки присутствия фильма на сервере
     *
     * @param id id фильма
     */
    private void containFilmId(Long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new FilmUnknownException("Фильм с ID " + id + " не существует");
        }
    }
}
