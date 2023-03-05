package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.Constants.DESCENDING_ORDER;
import static ru.yandex.practicum.filmorate.Constants.SORTS;

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
    public List<Film> getPopularFilms(Integer count, String sort) {
        if (!SORTS.contains(sort)) {
            throw new IncorrectParameterException("Некорректное значение sort, введите: asc или desc");
        }
        if (count <= 0) {
            throw new IncorrectParameterException("Значение count не может быть меньше 1");
        }
        return getFilms()
                .values()
                .stream()
                .sorted((f0, f1) -> compare(f0, f1, sort))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Метод сортировки по убыванию/возрастанию
     *
     * @param f0   фильма №1
     * @param f1   фильма №2
     * @param sort сортировка убыванию/возрастания
     * @return отсортированный элемент
     */
    private int compare(Film f0, Film f1, String sort) {
        int result = f0.getLikes().size() - (f1.getLikes().size());
        if (sort.equals(DESCENDING_ORDER)) {
            result = -1 * result;
        }
        return result;
    }
}
