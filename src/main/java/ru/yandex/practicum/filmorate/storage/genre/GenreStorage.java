package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    /**
     * Метод получения всех жанров
     *
     * @return Список жанров
     */
    List<Genre> getAllGenres();

    /**
     * Метод получения названия жанра по id
     *
     * @param id id жанра
     * @return Объект жанра
     */
    Genre getGenreById(int id);

    /**
     * Метод добавления жанра по id фильма
     *
     * @param filmId id фильма
     * @param genres Список жанров
     */
    void addGenresByFilmId(long filmId, List<Genre> genres);

    /**
     * Метод удаления жанра по id фильма
     *
     * @param filmId id фильма
     */
    void deleteGenresByFilmId(long filmId);
}
