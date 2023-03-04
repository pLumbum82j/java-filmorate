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
        String sql = "MERGE INTO FRIENDS f USING (VALUES (?,?)) S(friend1, friend2)\n" +
       // String sql = "MERGE INTO FRIENDS f USING (VALUES (friend1,friend2))" +
                "ON f.FIRST_USER_ID = friend1 AND f.SECOND_USER_ID = friend2 \n" +
                "WHEN NOT MATCHED THEN INSERT VALUES ( friend1, friend2)";
        try {
            jdbcTemplate.update(sql, friend1, friend2);
        } catch (DataIntegrityViolationException e) {
            //log.debug("Пользователь с идентификатором {} или {} не найден.", friend1, friend2);
            throw new UserUnknownException(
                    String.format("Пользователь с идентификатором %d или %d не найден.", friend1, friend2));
        }
    }

    public void deleteFriends(long friend1, long friend2) {
        String sql = "MERGE INTO FRIENDS f USING (VALUES (?,?)) S(friend1, friend2)\n" +
                "ON f.FIRST_USER_ID = S.friend1 AND f.SECOND_USER_ID = S.friend2 \n" +
                "WHEN  MATCHED THEN DELETE";

        jdbcTemplate.update(sql, friend1, friend2);
    }

    public List<User> getUserFriends(long id) {
        String sql = "SELECT  UF.id, UF.email, UF.login, UF.name, UF.birthday " +
                "FROM FRIENDS F " +
                "LEFT JOIN USER_FILMORATE UF on f.FRIEND2_ID = UF.ID " +
                "WHERE FRIEND1_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
    }

    public List<User> getCommonFriends(long friend1, long friend2) {

        String sql2 = "SELECT UF.ID, UF.EMAIL, UF.LOGIN, UF.NAME, UF.BIRTHDAY \n" +
                "FROM FRIENDSHIP F\n" +
                "INNER JOIN USER_FILMORATE UF ON F.FRIEND2_ID = UF.ID\n" +
                "WHERE FRIEND1_ID = ? AND FRIEND2_ID IN (\n" +
                "        SELECT FRIEND2_ID\n" +
                "        FROM FRIENDSHIP\n" +
                "        WHERE FRIEND1_ID = ?\n" +
                "     )";

        return jdbcTemplate.query(sql2, (rs, rowNum) -> User.makeUser(rs), friend1, friend2);
    }

}

