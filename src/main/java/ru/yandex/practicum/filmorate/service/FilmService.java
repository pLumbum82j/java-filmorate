package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {


    /**
     * Метод получения списка фильмов
     *
     * @return Список филмьов
     */
    List<Film> getFilms();

    /**
     * Метод получения списка популярных фильмов
     *
     * @param count количество фильмов в списке
     * @param sort  сортировка по убыванию/возрастанию like
     * @return Список филмьов
     */
    List<Film> getPopularFilms(Integer count, String sort);

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
     * Метод добавления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    Film addLike(Long filmId, Long userId);

    /**
     * Метод обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    Film update(Film film);

    /**
     * Метод удаления Like фильму
     *
     * @param filmId id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    Film deleteLike(Long filmId, Long userId);

}
