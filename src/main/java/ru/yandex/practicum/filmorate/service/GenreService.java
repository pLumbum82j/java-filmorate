package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreUnknownException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    /**
     * Метод получения всех жанров
     *
     * @return Список жанров
     */
    public List<Genre> getAllGenres() {
        log.debug("Получен запрос на список GENRE ");
        return genreStorage.getAllGenres();
    }

    /**
     * Метод получения названия жанра по id
     *
     * @param id id жанра
     * @return Объект жанра
     */
    public Genre getGenreById(int id) {
        if (genreStorage.getGenreById(id) != null) {
            log.debug("Получен запрос на поиск GENRE с ID " + id);
            return genreStorage.getGenreById(id);
        } else {
            throw new GenreUnknownException("Genre с ID " + id + " не найден");
        }
    }
}
