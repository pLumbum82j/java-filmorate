package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Map;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Long, Film> getFilms() {
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet("select * from films");
        return null;
    }

    @Override
    public Film findFilmById(Long id) {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
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
            log.info("MPA с индентификатором {} не найден ", id);
            return null;
        }
    }
}
