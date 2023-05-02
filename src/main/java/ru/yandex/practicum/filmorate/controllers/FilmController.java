package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd | HH:mm:ss");
    private int id = 0;

    @GetMapping
    public Map<Integer, Film> getUsers() {
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Name не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Description не может быть длинее 200 символов.");
        }
        if (film.getReleaseDate().isBefore(movieBirthday)) {
            throw new ValidationException("ReleaseDate не может быть раньше 28 декабря 1895 г..");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("Duration не может быть отрицательным.");
        }
        films.put(getNextId(), film);
        film.setId(id);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Фильм с данным id не найден.");
        }
        return film;
    }

    public int getNextId() {
        id++;
        return id;
    }
}
