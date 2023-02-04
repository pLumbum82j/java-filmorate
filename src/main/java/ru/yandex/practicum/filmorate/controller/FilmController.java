package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * Класс Контроллер по энпоинту Films
 */
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }
    @GetMapping("/{id}")
    public Film getUsers(@PathVariable("id") Long id) {
        return filmService.findFilmById(id);
    }
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }


}
