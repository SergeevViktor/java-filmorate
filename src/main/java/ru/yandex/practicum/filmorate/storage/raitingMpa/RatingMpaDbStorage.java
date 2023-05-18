package ru.yandex.practicum.filmorate.storage.raitingMpa;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
@Primary
public class RatingMpaDbStorage extends DbStorage implements RatingMpaStorage {

    public RatingMpaDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public RatingMpa getRatingById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT raiting_id," +
                                                                     "name " +
                                                              "FROM RatingMpa " +
                                                              "WHERE raiting_id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException("not found mpa");
        }
    }

    @Override
    public Set<RatingMpa> getAllRating() {
        Set<RatingMpa> ratingMpaList = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT raiting_id," +
                                                                     "name " +
                                                              "FROM RatingMpa " +
                                                              "ORDER BY raiting_id");
        while (sqlRowSet.next()) {
            RatingMpa ratingMpa = mapToRow(sqlRowSet);
            ratingMpaList.add(ratingMpa);
        }
        return ratingMpaList;
    }

    private RatingMpa mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("raiting_id");
        String name = sqlRowSet.getString("name");
        return RatingMpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}
