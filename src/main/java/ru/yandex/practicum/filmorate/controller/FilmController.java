package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private Long id = 1L;

    @GetMapping()
    public List<Film> findAll() {
        log.debug("Получен запрос на список фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        if (isValid(film)) {
            film.setId(id);
            films.put(id, film);
            log.debug("Фильм {} создан", film.getName());
            id++;
        }
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("Фильм с указанным ID {} - не существует", film.getId());
            throw new FilmUnknownException("Фильм с ID" + film.getId() + " не существует");
        }
        if (isValid(film)) {
            films.put(film.getId(), film);
            log.debug("Фильм {} изменён", film.getName());
        }
        return film;
    }

    private boolean isValid(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            log.debug("Название фильма не может быть пустым");
            throw new ValidationException("Поле с названием фильма не должно быть пустым.");
        } else if (film.getDescription().length() > 200) {
            log.debug("Максимальная длина описания более 200 символов");
            throw new ValidationException("Максимальная длина описания фильма имеет более 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            log.debug("Продолжительность фильма отрицательная");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else {
            return true;
        }
    }
}
