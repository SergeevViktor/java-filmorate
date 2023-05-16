package ru.yandex.practicum.filmorate.storage.raitingMpa;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Set;

public interface RatingMpaStorage {
    RatingMpa getRatingById(int id);
    Set<RatingMpa> getAllRating();
}