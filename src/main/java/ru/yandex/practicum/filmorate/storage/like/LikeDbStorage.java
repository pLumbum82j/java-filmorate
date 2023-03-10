package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "MERGE INTO LIKES L USING (VALUES (?,?)) S(filmId, userId)" +
                "ON L.USER_ID = filmId AND L.FILM_ID = userId " +
                "WHEN NOT MATCHED THEN INSERT(USER_ID, FILM_ID) VALUES(filmId, userId) ";
        try {
            jdbcTemplate.update(sqlQuery, userId, filmId);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectParameterException(
                    String.format("Фильм с id = %d или Пользователь с id = %d не найден.", filmId, userId));
        }
        String sqlQuery2 = "UPDATE FILMS SET LIKE_AMOUT = CASE WHEN LIKE_AMOUT IS NULL THEN 1 ELSE (LIKE_AMOUT + 1) END WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery2, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId);
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectParameterException(
                    String.format("Фильм с id = %d или Пользователь с id = %d не найден.", filmId, userId));
        }
        String sqlQuery2 = "UPDATE FILMS SET LIKE_AMOUT = LIKE_AMOUT - 1 WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery2, filmId);
    }
}

