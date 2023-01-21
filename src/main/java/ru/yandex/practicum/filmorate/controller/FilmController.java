package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс Контроллер по энпоинту Films
 */
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
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
    @GetMapping()
    public List<Film> findAll() {
        log.debug("Получен запрос на список фильмов");
        return new ArrayList<>(films.values());
    }

    /**
     * Метод (эндпоинт) создания фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.debug("Фильм {} создан", film.getName());
        return film;
    }

    /**
     * Метод (эндпоинт) обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с указанным ID {} - не существует", film.getId());
            throw new FilmUnknownException("Фильм с ID " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        log.debug("Фильм {} изменён", film.getName());

        return film;
    }
}
