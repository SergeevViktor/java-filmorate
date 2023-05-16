package ru.yandex.practicum.filmorate.storage.likes;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikesDbStorage extends DbStorage implements LikesStorage {

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Set<Likes> getLikesFilmId(int filmId) {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "user_id " +
                                                              "FROM Likes " +
                                                              "WHERE film_id = ?", filmId);
        while (sqlRowSet.next()) {
            var like = mapToRow(sqlRowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Likes getLikesUserForFilm(int userId, int filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "user_id " +
                                                              "FROM Likes " +
                                                              "WHERE user_id = ? " +
                                                              "AND film_id = ?", userId, filmId);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            return null;
        }
    }

    @Override
    public void delete(Likes likes) {
        var sqlQuery = "DELETE FROM Likes WHERE film_id = ? " +
                                         "AND user_id = ?";
        jdbcTemplate.update(sqlQuery, likes.getFilmId(), likes.getUserId());
    }

    @Override
    public Likes add(Likes likes) {

        String sql = "INSERT INTO Likes (film_id, user_id) " +
                     "VALUES(?, ?)";
        jdbcTemplate.update(sql,
                likes.getFilmId(),
                likes.getUserId());
        return likes;
    }

    @Override
    public Set<Likes> getAllLikes() {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "user_id " +
                                                              "FROM Likes");
        while (sqlRowSet.next()) {
            Likes like = mapToRow(sqlRowSet);
            likes.add(like);
        }
        return likes;
    }

    private Likes mapToRow(SqlRowSet sqlRowSet) {
        int filmId = sqlRowSet.getInt("film_id");
        int userId = sqlRowSet.getInt("user_id");
        return Likes.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
    }
}
