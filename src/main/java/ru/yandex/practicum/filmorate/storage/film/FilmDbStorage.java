package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.DbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.raitingMpa.RatingMpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository("FilmDbStorage")
@Primary
public class FilmDbStorage extends DbStorage implements FilmStorage {
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingMpaStorage ratingMpaStorage,
                         GenreStorage genreStorage, LikesStorage likesStorage) {
        super(jdbcTemplate);
        this.ratingMpaStorage = ratingMpaStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public List<Film> getListFilm() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT film_id," +
                            "name," +
                            "description," +
                            "release_date," +
                            "duration," +
                            "raiting_id " +
                     "FROM Films";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Film film = mapToRow(sqlRowSet);
            films.add(film);
        }
        log.info("Количество фильмов: {}", films.size());
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Films")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("raiting_id", film.getMpa().getId());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        film.setId(userKey.intValue());
        return film;
    }

    @Override
    public Film save(Film film) {
        String updateSql = "UPDATE Films SET name = ?," +
                                            "description = ?," +
                                            "release_date = ?," +
                                            "duration = ?," +
                                            "raiting_id = ? " +
                                        "WHERE film_id = ?";
        if (jdbcTemplate.update(updateSql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        ) <= 0) {
            log.error("Фильм не найден {}", film.getId());
            throw new ObjectNotFoundException("Фильм не найден");
        } else {
            return film;
        }
    }

    @Override
    public void deleteAllFilms() {
        String sql = "DELETE FROM Films";
        jdbcTemplate.update(sql);
    }

    @Override
    public Film getFilmById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT film_id," +
                                                                     "name," +
                                                                     "description," +
                                                                     "release_date," +
                                                                     "duration," +
                                                                     "raiting_id " +
                                                              "FROM Films " +
                                                              "WHERE film_id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
        }
    }

    private Film mapToRow(SqlRowSet sqlRowSet) {
        int mpaId = sqlRowSet.getInt("raiting_id");
        int id = sqlRowSet.getInt("film_id");
        String name = sqlRowSet.getString("name");
        String description = sqlRowSet.getString("description");
        LocalDate date = sqlRowSet.getDate("release_date").toLocalDate();
        int duration = sqlRowSet.getInt("duration");
        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
                .build();

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(date)
                .duration(duration)
                .mpa(mpa)
                .build();
    }
}