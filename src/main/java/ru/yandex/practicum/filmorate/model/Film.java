package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.MovieBirthday;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.Constants.FIRST_RELEASE;

@Data
@Builder
public class Film {
    private final Set<Long> likes = new HashSet<>();
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания более 200 символов")
    private String description;
    @MovieBirthday(value = FIRST_RELEASE)
    private LocalDate releaseDate;
    @Min(value = 1, message = "Продолжительность фильма нулевая или отрицательная")
    private long duration;

}
