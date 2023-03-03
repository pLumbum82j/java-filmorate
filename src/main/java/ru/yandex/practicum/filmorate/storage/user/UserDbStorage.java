package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
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

    @Override
    public boolean isContainUserId(Long id) {
        return false;
    }
}
