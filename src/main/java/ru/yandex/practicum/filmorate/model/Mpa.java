package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class Mpa {
    private Integer id;
    private String name;


    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
