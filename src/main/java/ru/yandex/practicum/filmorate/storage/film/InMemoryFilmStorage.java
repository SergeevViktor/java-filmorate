package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

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
    public Map<Integer, Film> getFilms() {
        return films;
    }
    @Override
    public List<Film> getListFilm() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(getNextId());
        return films.put(film.getId(), film);
    }

    @Override
    public Film save(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    @Override
    public Film getFilmById(int id) {
        if (!films.containsKey(id)) {
            log.error("ERROR: ID-{} не найден!", id);
            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
        }
        return films.get(id);
    }

    private int getNextId() {
        id++;
        return id;
    }
}