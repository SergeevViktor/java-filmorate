package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.filmService.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getUsers() {
        log.info("Получен GET-запрос - /films.");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Film findFilmById(@PathVariable int id) {
        log.info("Получен GET-запрос - /films/{id}.");
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getTopOfFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен GET-запрос - /films/popular.");
        return filmService.getTopFilms(count);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление данных по фильму: {}", film.getName());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void setLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь поставил лайк фильму: {}", filmService.getFilmById(id).getName());
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Удален лайк с фильма: {}", filmService.getFilmById(id).getName());
        filmService.deleteLike(id, userId);
    }
}