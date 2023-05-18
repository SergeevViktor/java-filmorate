package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage extends DbStorage implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Genre getGenreById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT genre_id," +
                                                                     "name " +
                                                              "FROM Genre " +
                                                              "WHERE genre_id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException("not found mpa");

        }
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT genre_id," +
                                                                     "name " +
                                                              "FROM Genre " +
                                                              "ORDER BY genre_id");
        while (sqlRowSet.next()) {
            Genre genre = mapToRow(sqlRowSet);
            genres.add(genre);
        }
        return genres;
    }

    private Genre mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("genre_id");
        String name = sqlRowSet.getString("name");
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}
