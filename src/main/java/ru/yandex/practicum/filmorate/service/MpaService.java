package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaUnknownException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;


    /**
     * Метод получения всех MPA
     *
     * @return Список MPA
     */
    public List<Mpa> getAllMpa() {
        log.debug("Получен запрос на список MPA ");
        return mpaStorage.getAllMpa();
    }

    /**
     * Метод получения названия MPA по id
     *
     * @param id id MPA
     * @return объект MPA
     */
    public Mpa getMpaById(int id) {
        if (mpaStorage.getMpaById(id) != null) {
            log.debug("Получен запрос на поиск MPA с ID " + id);
            return mpaStorage.getMpaById(id);
        } else {
            throw new MpaUnknownException("MPA с ID " + id + " не найден");
        }
    }
}
