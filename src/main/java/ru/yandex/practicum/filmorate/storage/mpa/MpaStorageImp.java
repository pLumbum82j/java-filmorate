package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
public class MpaStorageImp implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaStorageImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Mpa(
                        rs.getInt("mpa_id"),
                        rs.getString("name"),
                        rs.getString("description")));
    }


    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (mpaRows.next()) {
            //log.info("Найден MPA {} {}", mpaRows.getString("mpa_id")), mpaRows.getString("mpa_name");
            Mpa mpa = new Mpa(
                    mpaRows.getInt("mpa_id"),
                    mpaRows.getString("name"),
                    mpaRows.getString("description"));
            return mpa;
        } else {
            log.info("MPA с идентификатором {} не найден ", id);
            return null;
        }
    }
}
