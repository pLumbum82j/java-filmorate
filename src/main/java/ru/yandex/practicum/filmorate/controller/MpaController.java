package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    /**
     * Метод (эндпоинт) получения всех MPA
     *
     * @return Список MPA
     */
    @GetMapping
    public List<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

    /**
     * Метод (эндпоинт) получения названия MPA по id
     *
     * @param id id MPA
     * @return объект MPA
     */
    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") int id) {
        return mpaService.getMpaById(id);
    }

}
