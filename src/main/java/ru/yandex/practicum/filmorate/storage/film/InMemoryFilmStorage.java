package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
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


    public Map<Long, Film> getFilms() {
        return films;

    }


    public Film findFilmById(Long id) {
        return films.get(id);
    }

    public Film create(Film film) {
        film.setId(generatorId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public boolean addLike(Long filmId, Long userId) {
        return false;
    }


}
