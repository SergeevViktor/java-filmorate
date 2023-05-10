package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @Override
    public List<Film> getUsers() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.warn("ReleaseDate не может быть раньше {}.", movieBirthday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            throw new ValidationException("ReleaseDate не может быть раньше 28 декабря 1895 г..");
        }
        films.put(getNextId(), film);
        film.setId(id);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Данные по фильму обновлены.");
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм с ID-{} не найден.", film.getId());
            throw new ValidationException("Фильм с данным id не найден.");
        }
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Данные по фильму удалены.");
            films.remove(film.getId());
        } else {
            log.warn("Фильм с ID-{} не найден.", film.getId());
            throw new ValidationException("Фильм с данным id не найден.");
        }
    }

    private int getNextId() {
        id++;
        return id;
    }
}
