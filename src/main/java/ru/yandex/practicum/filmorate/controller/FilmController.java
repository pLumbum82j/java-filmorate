package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmDbService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;

/**
 * Класс Контроллер по энпоинту Films
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

   private final FilmDbService filmDbService;

    /**
     * Метод (эндпоинт) получения списка фильмов
     *
     * @return Список филмьов
     */
    @GetMapping()
    public List<Film> getFilms() {
        return filmDbService.getAllFilms();
    }

    /**
     * Метод (эндпоинт) получения списка популярных фильмов
     *
     * @param count количество фильмов в списке
     * @param sort  сортировка по убыванию/возрастанию like
     * @return Список филмьов
     */
//    @GetMapping("/popular")
//    public List<Film> getPopularFilms(
//            @RequestParam(value = "count", defaultValue = "10") Integer count,
//            @RequestParam(value = "sort", defaultValue = DESCENDING_ORDER) String sort) {
//        return filmDbService.getPopularFilms(count, sort);
//    }

    /**
     * Метод (эндпоинт) получения фильма по id
     *
     * @param id id фильма
     * @return объект фильма
     */
    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long id) {
        return filmDbService.findFilmById(id);
    }

    /**
     * Метод (эндпоинт) создания фильма
     *
     * @param film принятый объект фильма по эндпоинту
     * @return созданный объект фильма
     */
    @PostMapping()
    public Film create(@Valid @RequestBody Film film) throws SQLException {
        return filmDbService.create(film);
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
        return filmDbService.addLike(id, userId);
    }

    /**
     * Метод (эндпоинт) обновления фильма
     *
     * @param film Принятый объект фильма по эндпоинту
     * @return изменённый объект фильма
     */
    @PutMapping()
    public Film update(@Valid @RequestBody Film film) throws SQLException {
        return filmDbService.update(film);
    }
//
//    /**
//     * Метод (эндпоинт) удаления Like фильму
//     *
//     * @param id     id фильма
//     * @param userId id пользователя
//     * @return изменённый объект фильма
//     */
//    @DeleteMapping("/{id}/like/{userId}")
//    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
//        return filmService.deleteLike(id, userId);
//    }

}
