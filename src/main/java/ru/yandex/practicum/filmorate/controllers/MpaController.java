package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.filmService.FilmService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@AllArgsConstructor
public class MpaController {
    private final FilmService filmService;

    @GetMapping
    public List<RatingMpa> getMpaList() {
        return filmService.getMpaList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RatingMpa getMpa(@PathVariable int id) {
        return filmService.getMpa(id);
    }
}