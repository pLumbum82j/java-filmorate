package ru.yandex.practicum.filmorate.storage.film;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertIntoFilm;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertIntoFilm = new SimpleJdbcInsert(this.jdbcTemplate).withTableName("films").usingGeneratedKeyColumns("film_id");
    }

    @Override
    public Map<Long, Film> getFilms() {
        Map<Long, Film> result = new HashMap<>();
//        String sqlQuery = "SELECT * FROM FILMS";
//        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
//        return null;
        String sqlQuery = "SELECT F.*,M.NAME as MNAME FROM FILMS F JOIN MPA M ON F.MPA_ID = M.MPA_ID";
        List<Film> filmLists = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum));
//                        new Film(
//                                rs.getLong("FILM_ID"),
//                                rs.getString("F.NAME"),
//                                rs.getString("description"),
//                                rs.getDate("RELEASEDATE").toLocalDate(),
//                                rs.getLong("DURATION"),
//                                new Mpa(rs.getInt("MPA_ID"), rs.getString("M.NAME")),
//null
//                        ));
        for (Film film : filmLists) {
            result.put(film.getId(), film);
        }

        return result;

    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {

        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASEDATE").toLocalDate())
                .duration(rs.getLong("DURATION"))
                .mpa(new Mpa(rs.getInt("MPA_ID"), rs.getString("MNAME")))
                .rate(rs.getInt("RATE"))
                .genres(getGenreByFilmId(rs.getLong("FILM_ID")))
                .build();
    }

    private List<Genre> getGenreByFilmId(Long filmId) {
        String sqlQuery = "SELECT G.GENRE_ID, G.NAME FROM FILMS F JOIN GENRE_REG GR ON F.FILM_ID = GR.FILM_ID JOIN GENRE G ON GR.GENRE_ID = G.GENRE_ID WHERE F.FILM_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, filmId);
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME")));
        }
        return genres;
        //return new ArrayList<>();
    }

    @Override
    public Film findFilmById(Long id) {
        String sqlQuery = "SELECT F.*,M.NAME as MNAME FROM FILMS F JOIN MPA M ON F.MPA_ID = M.MPA_ID WHERE F.FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
//        Film film = Film.builder().build();
//        if (filmRows.next()) {
//            film.setId(id);
//            film.setName(filmRows.getString("NAME"));
//            film.setReleaseDate(filmRows.getDate("RELEASEDATE").toLocalDate());
//            film.setDuration(filmRows.getLong("DURATION"));
//            film.setDescription(filmRows.getString("DESCRIPTION"));
//            film.setMpa(new Mpa(filmRows.getInt("MPA_ID"), (filmRows.getString("MNAME"))));
//            film.setRate(filmRows.getInt("RATE"));
//            film.setGenres(getGenreByFilmId(filmRows.getLong("FILM_ID")));
//        }
//        return film;

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
            return film;
        }
        return null;


    }

    @Override
    public Film create(Film film) throws SQLException {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("NAME", film.getName());
        parameters.put("DESCRIPTION", film.getDescription());
        parameters.put("RELEASEDATE", film.getReleaseDate());
        parameters.put("DURATION", film.getDuration());
        parameters.put("RATE", film.getRate());
        parameters.put("MPA_ID", film.getMpa().getId());
        Long filmId = (Long) insertIntoFilm.executeAndReturnKey(parameters);
        if (film.getGenres() != null) {
            addGenresByFilmId(filmId, film.getGenres());
        }
        return findFilmById(filmId);
    }

    public void addGenresByFilmId(long filmId, List<Genre> genres) throws SQLException {
//       for (Genre genre : genres) {
//           // String sqlQuery = "INSERT INTO GENRE_REG (GENRE_ID, FILM_ID) values (?,?)";
//            String sqlQuery = "MERGE INTO GENRE_REG G USING (VALUES (?,?)) S(GENRE_ID, FILM_ID)\n" +
//                    "ON G.GENRE_ID = S.GENRE_ID AND G.FILM_ID = S.FILM_ID \n" +
//                    "WHEN NOT MATCHED THEN INSERT VALUES (S.GENRE_ID, S.FILM_ID)";
//            jdbcTemplate.update(sqlQuery, genre.getId(), filmId);
//        }
//    }
    for (Genre genre : genres) {
            String sqlQuery = "INSERT INTO GENRE_REG (GENRE_ID, FILM_ID) values (?,?)";
            jdbcTemplate.update(sqlQuery, genre.getId(), filmId);
        }
    }


    @Override
    public Film update(Film film) throws SQLException {
        String sqlQuery = "UPDATE FILMS SET name=?, description=?, releasedate=?, duration=?,rate=?, mpa_id=? WHERE film_id=?";
        if (jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()) > 0) {
            deleteGenresByFilmId(film.getId()); // обновляем жанры
            if (film.getGenres() != null) {
                addGenresByFilmId(film.getId(), film.getGenres());
            }
            return findFilmById(film.getId());
        }
        return null;
    }

    public void deleteGenresByFilmId(long filmId) {
        String sqlQuery = "DELETE FROM GENRE_REG WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

}
