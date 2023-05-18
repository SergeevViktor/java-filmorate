package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository("UserDbStorage")
@Primary
public class UserDbStorage extends DbStorage implements UserStorage {

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<User> getListUser() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id," +
                            "email," +
                            "login," +
                            "name," +
                            "birthday " +
                     "FROM Users";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            User user = mapToRow(sqlRowSet);
            users.add(user);
        }
        log.info("Количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthday", user.getBirthday());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        user.setId(userKey.intValue());
        return user;
    }

    @Override
    public User save(User user) {
        String updateSql = "UPDATE Users SET email = ?," +
                                            "login = ?," +
                                            "name = ?," +
                                            "birthday = ? " +
                                        "WHERE user_id = ?";
        if (jdbcTemplate.update(updateSql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        ) <= 0) {
            log.error("Пользователь не найден {}", user.getId());
            throw new ObjectNotFoundException("Пользователь не найден");
        } else {
            return user;
        }
    }

    @Override
    public void deleteAllUsers() {
        String sql = "DELETE FROM Users";
        jdbcTemplate.update(sql);
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "SELECT user_id," +
                                "login," +
                                "email," +
                                "name," +
                                "birthday " +
                           "FROM Users " +
                           "WHERE user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, userRowMapper, id);
            log.info("Пользователь с id {} найден:", id);
            return user;
        } catch (Exception e) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
    }

    private final RowMapper<User> userRowMapper = (ResultSet resultSet, int rowNum) -> User.builder()
            .id(resultSet.getInt("user_id"))
            .login(resultSet.getString("login"))
            .email(resultSet.getString("email"))
            .name(resultSet.getString("name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();

    private User mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("user_id");
        String email = sqlRowSet.getString("email");
        String login = sqlRowSet.getString("login");
        String name = sqlRowSet.getString("name");
        LocalDate birthday = sqlRowSet.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}