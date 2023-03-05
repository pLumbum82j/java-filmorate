package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    /**
     * Метод (эндпоинт) получения всех жанров
     *
     * @return Список жанров
     */
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    /**
     * Метод (эндпоинт) получения названия жанра по id
     *
     * @param id id жанра
     * @return Объект жанра
     */
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") int id) {
        return genreService.getGenreById(id);
    }
}
