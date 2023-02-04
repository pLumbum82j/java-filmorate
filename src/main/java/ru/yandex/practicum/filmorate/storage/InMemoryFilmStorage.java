package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    /**
     * Генератор ID
     *
     * @return ID
     */
    private Long generatorId() {
        return id++;
    }

    /**
     * Метод (эндпоинт) получения списка фильмов
     *
     * @return Список филмьов
     */
    public Map<Long, Film> getFilms() {
        return films;
    }

    /**
     * Метод (эндпоинт) создания фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */

    public Film create(Film film) {
        film.setId(generatorId());
        films.put(film.getId(), film);
        return film;
    }

    /**
     * Метод (эндпоинт) обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */

    public Film update(Film film) {
        return films.put(film.getId(), film);
    }


}
