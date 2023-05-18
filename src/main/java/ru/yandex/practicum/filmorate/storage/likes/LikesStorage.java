package ru.yandex.practicum.filmorate.storage.likes;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Set;

public interface LikesStorage {
    Set<Likes> getLikesFilmId(int filmId);

    Likes getLikesUserForFilm(int userId, int filmId);

    void delete(Likes likes);

    Likes add(Likes likes);

    Set<Likes> getAllLikes();
}