package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertIntoUser;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertIntoUser = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("user_id");
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> result = new HashMap<>();
        String sql = "SELECT * FROM users";
        List<User> userLists = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs, rowNum));
        for (User user : userLists) {
            result.put(user.getId(), user);
        }
        return result;
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {

        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday(rs.getDate("BIRTHDAY").toLocalDate())
                .build();
    }


    @Override
    public User findUserById(Long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (userRows.next()) {
            User user = User.builder().build();
            user.setId(id);
            user.setEmail(userRows.getString("EMAIL"));
            user.setLogin(userRows.getString("LOGIN"));
            user.setName(userRows.getString("NAME"));
            user.setBirthday(userRows.getDate("BIRTHDAY").toLocalDate());
            return user;
        }
        return null;
    }


    @Override
    public User create(User user) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthday", user.getBirthday());
        Long userId = (Long) insertIntoUser.executeAndReturnKey(parameters);
        return findUserById(userId);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set email=?, login=?, name=?, birthday=? where user_id=?";
        if(jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()) > 0) {
            return user;
        }
        return null;
    }

    public void addFriends(long friend1, long friend2){
        String sql = "MERGE INTO FRIENDSHIP f USING (VALUES (?,?)) S(friend1, friend2)\n" +
                "ON f.FRIEND1_ID = S.friend1 AND f.FRIEND2_ID = S.friend2 \n" +
                "WHEN NOT MATCHED THEN INSERT VALUES ( S.friend1, S.friend2)";
        try{
            jdbcTemplate.update(sql,friend1, friend2);
        } catch ( DataIntegrityViolationException e ) {
            //log.debug("Пользователь с идентификатором {} или {} не найден.", friend1, friend2);
            throw new UserUnknownException(
                    String.format("Пользователь с идентификатором %d или %d не найден.", friend1,friend2));
        }
    }

    public void deleteFriends(long friend1, long friend2){
        String sql = "MERGE INTO FRIENDSHIP f USING (VALUES (?,?)) S(friend1, friend2)\n" +
                "ON f.FRIEND1_ID = S.friend1 AND f.FRIEND2_ID = S.friend2 \n" +
                "WHEN  MATCHED THEN DELETE";

        jdbcTemplate.update(sql,friend1, friend2);
    }

    public List<User> getUserFriends(long id){
        String sql =    "SELECT  UF.id, UF.email, UF.login, UF.name, UF.birthday " +
                "FROM FRIENDSHIP f " +
                "LEFT JOIN USER_FILMORATE UF on f.FRIEND2_ID = UF.ID " +
                "WHERE FRIEND1_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> User.makeUser(rs), id);
    }

    public List<User> getCommonFriends(long friend1, long friend2){

        String sql2 =   "SELECT UF.ID, UF.EMAIL, UF.LOGIN, UF.NAME, UF.BIRTHDAY \n" +
                "FROM FRIENDSHIP F\n" +
                "INNER JOIN USER_FILMORATE UF ON F.FRIEND2_ID = UF.ID\n" +
                "WHERE FRIEND1_ID = ? AND FRIEND2_ID IN (\n" +
                "        SELECT FRIEND2_ID\n" +
                "        FROM FRIENDSHIP\n" +
                "        WHERE FRIEND1_ID = ?\n" +
                "     )";

        return jdbcTemplate.query(sql2, (rs, rowNum) -> User.makeUser(rs), friend1, friend2);
    }


    @Override
    public boolean isContainUserId(Long id) {
        return false;
    }
}
