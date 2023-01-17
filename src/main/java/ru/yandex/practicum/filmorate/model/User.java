package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
    // @NotNull
    //@NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;
    private String name;
    //    @NotNull
    //@NotBlank
    @Past(message = "День рождения не может быть в будущем")
    private LocalDate birthday;
}
