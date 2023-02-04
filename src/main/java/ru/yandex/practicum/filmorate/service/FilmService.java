package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {

    FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    /**
     * Метод (эндпоинт) получения списка фильмов
     *
     * @return Список филмьов
     */
    public List<Film> getFilms() {
        log.debug("Получен запрос на список фильмов");
        return new ArrayList<>(filmStorage.getFilms().values());
    }
    public Film findFilmById(Long id){
        if (!filmStorage.isContainFilmId(id)) {
            log.warn("Фильм с указанным ID {} - не существует", id);
            throw new FilmUnknownException("Фильм с ID " + id + " не существует");
        }
        return filmStorage.findFilmById(id);
    }

    /**
     * Метод (эндпоинт) создания фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    public Film create(Film film) {
        filmStorage.create(film);
        log.debug("Фильм {} создан", film.getName());
        return film;
    }

    /**
     * Метод (эндпоинт) обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    public Film update(Film film) {
        if (!filmStorage.isContainFilmId(film.getId())) {
            log.warn("Фильм с указанным ID {} - не существует", film.getId());
            throw new FilmUnknownException("Фильм с ID " + film.getId() + " не существует");
        }
        filmStorage.update(film);
        log.debug("Фильм {} изменён", film.getName());

        return film;
    }


}
