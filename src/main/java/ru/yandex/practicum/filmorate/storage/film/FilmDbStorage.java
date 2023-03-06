package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertIntoFilm;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertIntoFilm = new SimpleJdbcInsert(this.jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> result = new HashMap<>();
        String sqlQuery = "SELECT F.*, M.NAME AS MNAME FROM FILMS F JOIN MPA M ON F.MPA_ID = M.MPA_ID";
        List<Film> filmLists = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
        for (Film film : filmLists) {
            result.put(film.getId(), film);
        }
        return result;

    }

    @Override
    public Film findFilmById(Long id) {
        String sqlQuery = "SELECT F.*,M.NAME as MNAME FROM FILMS F JOIN MPA M ON F.MPA_ID = M.MPA_ID WHERE F.FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (filmRows.next()) {
            Film film = Film.builder().build();
            film.setId(id);
            film.setName(filmRows.getString("NAME"));
            film.setReleaseDate(filmRows.getDate("RELEASEDATE").toLocalDate());
            film.setDuration(filmRows.getLong("DURATION"));
            film.setDescription(filmRows.getString("DESCRIPTION"));
            film.setMpa(new Mpa(filmRows.getInt("MPA_ID"), (filmRows.getString("MNAME"))));
            film.setRate(filmRows.getInt("RATE"));
            film.setGenres(getGenreByFilmId(filmRows.getLong("FILM_ID")));
            film.setLikeAmout(filmRows.getLong("LIKE_AMOUT"));
            return film;
        }
        return null;
    }

    @Override
    public List<Film> getPopularFilms(Integer count, String sort) {
        StringBuilder sqlQuery = new StringBuilder(
                "SELECT F.*,M.NAME as MNAME FROM FILMS F JOIN MPA M ON F.MPA_ID = M.MPA_ID ");
        sqlQuery.append("ORDER BY LIKE_AMOUT ").append(sort);
        sqlQuery.append(" LIMIT ").append(count);
        List<Film> filmLists = jdbcTemplate.query(sqlQuery.toString(), (rs, rowNum) -> makeFilm(rs));
        return filmLists;
    }

    @Override
    public Film create(Film film) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("RELEASEDATE", film.getReleaseDate());
        parameters.put("DURATION", film.getDuration());
        parameters.put("RATE", film.getRate());
        parameters.put("MPA_ID", film.getMpa().getId());
        parameters.put("LIKE_AMOUT", 0);
        Long filmId = (Long) insertIntoFilm.executeAndReturnKey(parameters);
        return findFilmById(filmId);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?,RATE = ?, MPA_ID = ? " +
                "WHERE FILM_ID=? ";

        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()) > 0) {
            return findFilmById(film.getId());
        }

        return null;
    }

    /**
     * Метод получения списка жанров фильма по id
     *
     * @param filmId id фильма
     * @return Список жанров
     */
    private List<Genre> getGenreByFilmId(Long filmId) {
        String sqlQuery = "SELECT G.GENRE_ID, G.NAME FROM FILMS F " +
                "JOIN GENRE_REG GR ON F.FILM_ID = GR.FILM_ID " +
                "JOIN GENRE G ON GR.GENRE_ID = G.GENRE_ID " +
                "WHERE F.FILM_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME")));
        }
        return genres;
    }

    /**
     * Метод создания объекта Film
     *
     * @param rs Данные SQL запроса
     * @return Объект Film
     */
    private Film makeFilm(ResultSet rs) {
        Film film;
        try {
            film = Film.builder()
                    .id(rs.getLong("FILM_ID"))
                    .name(rs.getString("NAME"))
                    .description(rs.getString("DESCRIPTION"))
                    .releaseDate(rs.getDate("RELEASEDATE").toLocalDate())
                    .duration(rs.getLong("DURATION"))
                    .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MNAME")))
                    .rate(rs.getInt("RATE"))
                    .likeAmout(rs.getLong("LIKE_AMOUT"))
                    .genres(getGenreByFilmId(rs.getLong("FILM_ID")))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return film;
    }
}
