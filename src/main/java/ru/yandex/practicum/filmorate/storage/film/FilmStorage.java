package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.sql.SQLException;
import java.util.Map;

/**
 * Интерфейс отвечаюзий за поведение объектов Film в памяти
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
    Film create(Film film) throws SQLException;

    /**
     * Метод обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    Film update(Film film);
}
