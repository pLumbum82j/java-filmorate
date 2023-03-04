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

    public void addFriends(long friend1, long friend2) {
        String sql = "MERGE INTO FRIENDS F USING (VALUES (?,?)) V (friend1, friend2)" +
                "ON F.FIRST_USER_ID = friend1 AND F.SECOND_USER_ID = friend2 " +
                "WHEN NOT MATCHED THEN INSERT (FIRST_USER_ID, SECOND_USER_ID)  VALUES (friend1, friend2)";
        try {
            jdbcTemplate.update(sql, friend1, friend2);
        } catch (DataIntegrityViolationException e) {
            //log.debug("Пользователь с идентификатором {} или {} не найден.", friend1, friend2);
            throw new UserUnknownException(
                    String.format("Пользователь с идентификатором %d или %d не найден.", friend1, friend2));
        }
    }

    public void deleteFriends(long friend1, long friend2) {
        String sql = "MERGE INTO FRIENDS F USING (VALUES (?,?)) V (friend1, friend2)" +
                "ON F.FIRST_USER_ID = friend1 AND F.SECOND_USER_ID = friend2 " +
                "WHEN  MATCHED THEN DELETE";

        jdbcTemplate.update(sql, friend1, friend2);
    }

    public List<User> getUserFriends(long id) {
        String sql = "SELECT  U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIENDS F " +
                "LEFT JOIN USERS U ON F.SECOND_USER_ID = U.USER_ID " +
                "WHERE F.FIRST_USER_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
    }

    public List<User> getListOfCommonFriends(long friend1, long friend2) {

        String sql2 = "SELECT  U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY " +
                "FROM FRIENDS F " +
                "INNER JOIN USERS U ON F.SECOND_USER_ID = U.USER_ID " +
                "WHERE F.FIRST_USER_ID = ? AND F.SECOND_USER_ID IN " +
                "(SELECT SECOND_USER_ID " +
                "FROM FRIENDS " +
                "WHERE FIRST_USER_ID = ?)";


        return jdbcTemplate.query(sql2, (rs, rowNum) -> User.makeUser(rs), friend1, friend2);
    }

}

