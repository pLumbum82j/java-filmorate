package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
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
        String sql2 = "UPDATE FILMS SET LIKE_AMOUT = LIKE_AMOUT + 1 WHERE FILM_ID = ?";
        jdbcTemplate.update(sql2, filmId);
    }

    public void deleteLike(long filmId, long userId){
//        if(userId < 0){
//            //log.debug("Пользователь с отрицательным id {} не может существовать.", userId);
//            throw new EntityDoesNotExistException(
//                    String.format("Пользователь с отрицательным id %d не может существовать.", userId));
//        }

        String sql = "DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        try{
            jdbcTemplate.update(sql, filmId, userId);
        } catch(DataIntegrityViolationException e) {
            //log.debug("Фильм с id = {} или Пользователь с id = {} не найден.", filmId, userId);
            throw new IncorrectParameterException(
                    String.format("Фильм с id = %d или Пользователь с id = %d не найден."
                            , filmId, userId));
        }
        String sql2 = "UPDATE FILMS SET LIKE_AMOUT = LIKE_AMOUT - 1 WHERE FILM_ID = ?";
        jdbcTemplate.update(sql2, filmId);
    }


}

