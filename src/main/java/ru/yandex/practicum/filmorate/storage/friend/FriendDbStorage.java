package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;


    public List<User> getUserFriends(long id) {
        String sqlQuery = "SELECT  U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIENDS F " +
                "LEFT JOIN USERS U ON F.SECOND_USER_ID = U.USER_ID " +
                "WHERE F.FIRST_USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> User.makeUser(rs), id);
    }

    public List<User> getListOfCommonFriends(long firstId, long secondId) {

        String sqlQuery = "SELECT  U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIENDS F " +
                "INNER JOIN USERS U ON F.SECOND_USER_ID = U.USER_ID " +
                "WHERE F.FIRST_USER_ID = ? AND F.SECOND_USER_ID IN " +
                "(SELECT SECOND_USER_ID " +
                "FROM FRIENDS " +
                "WHERE FIRST_USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> User.makeUser(rs), firstId, secondId);
    }

    public void addFriends(long firstId, long secondId) {
        String sqlQuery = "MERGE INTO FRIENDS F USING (VALUES (?,?)) V (firstId, secondId)" +
                "ON F.FIRST_USER_ID = firstId AND F.SECOND_USER_ID = secondId " +
                "WHEN NOT MATCHED THEN INSERT (FIRST_USER_ID, SECOND_USER_ID)  VALUES (firstId, secondId)";
        try {
            jdbcTemplate.update(sqlQuery, firstId, secondId);
        } catch (DataIntegrityViolationException e) {
            throw new UserUnknownException(
                    String.format("Пользователь с идентификатором %d или %d не найден.", firstId, secondId));
        }
    }

    public void deleteFriends(long firstId, long secondId) {
        String sqlQuery = "MERGE INTO FRIENDS F USING (VALUES (?,?)) V (firstId, secondId)" +
                "ON F.FIRST_USER_ID = firstId AND F.SECOND_USER_ID = secondId " +
                "WHEN  MATCHED THEN DELETE";
        try {
            jdbcTemplate.update(sqlQuery, firstId, secondId);
        } catch (DataIntegrityViolationException e) {
            throw new UserUnknownException(
                    String.format("Пользователь с идентификатором %d или %d не найден.", firstId, secondId));
        }
    }


}

