package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("DbStorage")
@AllArgsConstructor
public abstract class DbStorage {
    protected final JdbcTemplate jdbcTemplate;
}