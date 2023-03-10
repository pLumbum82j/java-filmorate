package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Qualifier;
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

    private final FilmService filmService;

    public FilmController(@Qualifier("filmDbService") FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Метод (эндпоинт) получения списка фильмов
     *
     * @return Список филмьов
     */
    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    /**
     * Метод (эндпоинт) получения списка популярных фильмов
     *
     * @param count количество фильмов в списке
     * @param sort  сортировка по убыванию/возрастанию like
     * @return Список фильмов
     */
    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(value = "count", defaultValue = "10") Integer count,
            @RequestParam(value = "sort", defaultValue = DESCENDING_ORDER) String sort) {
        return filmService.getPopularFilms(count, sort);
    }

    /**
     * Метод (эндпоинт) получения фильма по id
     *
     * @param id id фильма
     * @return объект фильма
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        return filmService.findFilmById(id);
    }

    /**
     * Метод (эндпоинт) создания фильма
     *
     * @param film принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        return filmService.create(film);
    }

    /**
     * Метод (эндпоинт) добавления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.addLike(id, userId);
    }

    /**
     * Метод (эндпоинт) обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    /**
     * Метод (эндпоинт) удаления Like фильму
     *
     * @param id     id фильма
     * @param userId id пользователя
     * @return изменённый объект фильма
     */
    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }
}
