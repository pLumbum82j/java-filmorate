package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaUnknownException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

@Service
public class FilmDbService {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmDbService(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }



}

