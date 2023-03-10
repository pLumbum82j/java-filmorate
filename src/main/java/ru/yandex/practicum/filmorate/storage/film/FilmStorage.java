package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Интерфейс отвечающий за поведение объектов Film в памяти
 */
public interface FilmStorage {

    /**
     * Метод получения списка фильмов
     *
     * @return Список филмьов
     */
    Map<Long, Film> getFilms();

    /**
     * Метод получения фильма по id
     *
     * @param id id фильма
     * @return объект фильма
     */
    Film findFilmById(Long id);

    /**
     * Метод создания фильма
     *
     * @param film принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    Film create(Film film);

    /**
     * Метод обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    Film update(Film film);

    /**
     * Метод получения списка популярных фильмов
     *
     * @param count количество фильмов в списке
     * @param sort  сортировка по убыванию/возрастанию like
     * @return Список фильмов
     */
    List<Film> getPopularFilms(Integer count, String sort);
}
