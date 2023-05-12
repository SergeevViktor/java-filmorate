package ru.yandex.practicum.filmorate.service.filmService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Slf4j
@Component
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.getFilms().containsKey(film.getId())) {
            validateFilm(film);
            filmStorage.save(film);
        } else {
            log.error("ERROR: ID-{} не найден!", film.getId());
            throw new ObjectNotFoundException("Такого фильма не существует!");
        }
        return film;

    }

    @Override
    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmStorage.getListFilm());
    }

    @Override
    public void addLike(int userId, int filmId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        film.addLike(user);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        film.deleteLike(user);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getListFilm().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

    }
}