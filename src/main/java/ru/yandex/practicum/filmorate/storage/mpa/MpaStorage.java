package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    /**
     * Метод получения всех MPA
     *
     * @return Список MPA
     */
    List<Mpa> getAllMpa();

    /**
     * Метод получения названия MPA по id
     *
     * @param id id MPA
     * @return объект MPA
     */
    Mpa getMpaById(int id);
}
