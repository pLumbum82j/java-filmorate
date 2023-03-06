package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("NAME")));
    }

    @Override
    public Genre getGenreById(int id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (genreRows.next()) {
            return new Genre(
                    genreRows.getInt("GENRE_ID"),
                    genreRows.getString("NAME"));
        }
        return null;
    }

    @Override
    public void addGenresByFilmId(long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            String sqlQuery = "MERGE INTO GENRE_REG G USING (VALUES (?,?)) S(GENRE_ID, FILM_ID) " +
                    "ON G.GENRE_ID = S.GENRE_ID AND G.FILM_ID = S.FILM_ID " +
                    "WHEN NOT MATCHED THEN INSERT (GENRE_ID, FILM_ID) VALUES (S.GENRE_ID, S.FILM_ID) ";
            try {
                jdbcTemplate.update(sqlQuery, genre.getId(), filmId);
            } catch (DataIntegrityViolationException e) {
                throw new FilmUnknownException(
                        String.format("Фильм с идентификатором %d .", filmId));
            }
        }
    }

    public void deleteGenresByFilmId(long filmId) {
        String sqlQuery = "DELETE FROM GENRE_REG WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }
}
