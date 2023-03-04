package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO LIKES (USER_ID, FILM_ID) values (?,?)";

        //return
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }
}
