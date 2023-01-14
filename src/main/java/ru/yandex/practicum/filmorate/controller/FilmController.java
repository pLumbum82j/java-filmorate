package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private long id = 1;

    @GetMapping()
    public Map<Integer, Film> findAll() {
        return films;
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        film.setId(id);
        films.put((int) id, film);
        id++;
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        films.put((int) film.getId(), film);
        return film;
    }
}
