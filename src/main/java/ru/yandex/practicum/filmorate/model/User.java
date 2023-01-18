package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;
//    @Email(message = "Не корректный адрес электронной почты")
//    @NotBlank(message = "Поле email не может быть пустым")
    private String email;
//    @NotBlank(message = "Поле Login не может быть пустым")
    private String login;
    private String name;
//    @Past(message = "День рождения не может быть в будущем")
//    @NotNull(message = "День рождение не может быть пустым")
    private LocalDate birthday;
}
