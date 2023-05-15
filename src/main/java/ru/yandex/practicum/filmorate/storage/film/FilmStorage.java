package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getFilms();

    List<Film> getListFilm();

    Film getFilmById(int id);

    Film save(Film film);

    Film addFilm(Film film);

    void deleteAllFilms();

}