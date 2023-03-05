package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Long> friendsList = new HashSet<>();

    /**
     * Статический метод создания объекта User
     *
     * @param rs Данные SQL результата
     * @return Объект User
     */
    public static User makeUser(ResultSet rs) {
        User user;
        try {
            long id = rs.getLong("USER_ID");
            String email = rs.getString("EMAIL");
            String login = rs.getString("LOGIN");
            String name = rs.getString("NAME");
            LocalDate birthday = rs.getDate("BIRTHDAY").toLocalDate();

            user = User.builder()
                    .id(id)
                    .email(email)
                    .login(login)
                    .name(name)
                    .birthday(birthday)
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}

