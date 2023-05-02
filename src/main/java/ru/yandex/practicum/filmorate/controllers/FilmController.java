package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<Film> getUsers() {
        log.info("Получен GET-запрос - /films.");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен POST-запрос - /films.");
        LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);

        if (film.getReleaseDate().isBefore(movieBirthday)) {
            log.warn("ReleaseDate не может быть раньше {}.", movieBirthday.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            throw new ValidationException("ReleaseDate не может быть раньше 28 декабря 1895 г..");
        }
        films.put(getNextId(), film);
        film.setId(id);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен PUT-запрос - /films.");
        if (films.containsKey(film.getId())) {
            log.info("Данные по фильму обновлены.");
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм с ID-{} не найден.", film.getId());
            throw new ValidationException("Фильм с данным id не найден.");
        }
        return film;
    }

    public int getNextId() {
        id++;
        return id;
    }
}