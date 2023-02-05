package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;

/**
 * Класс Контроллер по энпоинту Films
 */
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count,
            @RequestParam(value = "sort", defaultValue = DESCENDING_ORDER, required = false) String sort) {
        return filmService.getPopularFilms(count, sort);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
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

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

}
