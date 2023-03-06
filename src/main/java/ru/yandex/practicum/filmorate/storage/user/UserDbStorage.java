package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

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
        insertIntoUser = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("USERS").usingGeneratedKeyColumns("USER_ID");
    }

    @Override
    public Map<Long, User> getUsers() {
        Map<Long, User> result = new HashMap<>();
        String sqlQuery = "SELECT * FROM USERS";
        List<User> userLists = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> User.makeUser(rs));
        for (User user : userLists) {
            result.put(user.getId(), user);
        }
        return result;
    }

    @Override
    public User findUserById(Long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE USER_ID = ?";
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
        parameters.put("EMAIL", user.getEmail());
        parameters.put("LOGIN", user.getLogin());
        parameters.put("NAME", user.getName());
        parameters.put("BIRTHDAY", user.getBirthday());
        Long userId = (Long) insertIntoUser.executeAndReturnKey(parameters);
        return findUserById(userId);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? where USER_ID=?";
        if (jdbcTemplate.update(sqlQuery,
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
        User user = findUserById(id);
        return user != null;
    }
}
