package ru.yandex.practicum.filmorate.storage.filmGenre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmGenreDbStorage extends DbStorage implements FilmGenreStorage {

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void deleteByFilmId(int filmId) {
        var sqlQuery = "DELETE FROM FilmGenre WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public FilmGenre add(FilmGenre filmGenre) {
        String sql = "INSERT INTO FilmGenre (film_id, genre_id) " +
                     "VALUES(?, ?)";
        jdbcTemplate.update(sql,
                filmGenre.getFilmId(),
                filmGenre.getGenreId());
        return filmGenre;
    }

    @Override
    public List<FilmGenre> getLikesFilmId(int filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "genre_id " +
                                                              "FROM FilmGenre " +
                                                              "WHERE film_id = ?", filmId);
        while (sqlRowSet.next()) {
            var filmGenre = mapToRow(sqlRowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    @Override
    public List<FilmGenre> getAllFilmGenre() {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "genre_id " +
                                                              "FROM FilmGenre");
        while (sqlRowSet.next()) {
            FilmGenre filmGenre = mapToRow(sqlRowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    private FilmGenre mapToRow(SqlRowSet sqlRowSet) {
        int genreId = sqlRowSet.getInt("genre_id");
        int filmId = sqlRowSet.getInt("film_id");
        return FilmGenre.builder()
                .genreId(genreId)
                .filmId(filmId)
                .build();
    }
}
