package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.validator.MovieBirthday;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.FIRST_RELEASE;

@Data
@Builder
public class Film {
/*
    public Film() {

    }

    public Film(String name) {
        this.name = name;
    }


    public Film(String name, String description, LocalDate releaseDate, long duration) {
                this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, long duration) {
            this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, long duration, Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Film(Long id) {
        this.id = id;
    }
*/
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания более 200 символов")
    private String description;
    @MovieBirthday(value = FIRST_RELEASE)
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма нулевая или отрицательная")
    private long duration;
    private final Set<Long> likes = new HashSet<>();

    private Mpa mpa;
    private List<Genre> genres;

}
