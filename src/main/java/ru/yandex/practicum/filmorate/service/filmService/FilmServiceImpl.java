package ru.yandex.practicum.filmorate.service.filmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmGenre.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;
import ru.yandex.practicum.filmorate.storage.raitingMpa.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateFilm;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        addGenreForFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);
        try {
            filmStorage.save(film);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Такого фильма не существует!");
        }
        filmGenreStorage.deleteByFilmId(film.getId());
        addGenreForFilm(film);
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        var film = filmStorage.getFilmById(id);
        var mpaList = ratingMpaStorage.getAllRating();
        var genres = genreStorage.getAllGenres();
        var filmGenres = filmGenreStorage.getLikesFilmId(film.getId());
        var likes = likesStorage.getLikesFilmId(film.getId());

        setMpaGenreLikesForFilm(film, mpaList, genres, filmGenres, likes);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        var films = filmStorage.getListFilm();
        var mpaList = ratingMpaStorage.getAllRating();
        var genres = genreStorage.getAllGenres();
        var filmGenres = filmGenreStorage.getAllFilmGenre();
        var likes = likesStorage.getAllLikes();
        for (var film : films) {
            setMpaGenreLikesForFilm(film, mpaList, genres, filmGenres, likes);
        }
        return films;
    }

    @Override
    public void addLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesUserForFilm(userId, filmId);
        if (like == null) {
            likesStorage.add(new Likes(filmId, userId));
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesUserForFilm(userId, filmId);
        if (like == null) {
            throw new ObjectNotFoundException("doesn't found!");
        }
        likesStorage.delete(new Likes(filmId, userId));
    }

    @Override
    public List<Film> getTopFilms(int count) {
        var films = getFilms();
        return films.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenre(int id) {
        return genreStorage.getGenreById(id);
    }

    @Override
    public List<Genre> getGenreList() {
        return genreStorage.getAllGenres();
    }

    @Override
    public RatingMpa getMpa(int id) {
        return ratingMpaStorage.getRatingById(id);
    }

    @Override
    public List<RatingMpa> getMpaList() {
        var mpaList = ratingMpaStorage.getAllRating();
        List<RatingMpa> list = new ArrayList<>(mpaList);
        Collections.sort(list, Comparator.comparing(RatingMpa::getId));
        return list;
    }

    private void addGenreForFilm(Film film) {
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            List<Genre> ratingList = film.getGenres();
            Set<Integer> set = new HashSet<>();
            ratingList.removeIf(rating -> !set.add(rating.getId()));
            film.setGenres(ratingList);

            for (var genre : film.getGenres()) {
                var filmGenre = new FilmGenre(film.getId(), genre.getId());
                filmGenreStorage.add(filmGenre);
            }
        }
    }

    private void setMpaGenreLikesForFilm(Film film, Set<RatingMpa> mpaList, List<Genre> genres,
                                         List<FilmGenre> filmGenres, Set<Likes> likes) {
        List<Genre> genreByFilm = new ArrayList<>();
        filmGenres.stream().filter(f -> f.getFilmId() == film.getId())
                .forEach(f -> genreByFilm.add(
                        new Genre(f.getGenreId(),
                                genres.stream().filter(g -> g.getId() == f.getGenreId()).findAny().get().getName())));

        film.setGenres(genreByFilm);

        film.getMpa().setName(mpaList.stream().filter(m -> m.getId() == film.getMpa().getId()).findAny().get().getName());

        Set<Integer> likesByFilm = new HashSet<>();
        likes.stream().filter(l -> l.getFilmId() == film.getId()).forEach(l -> likesByFilm.add(l.getUserId()));
        film.setLikes(likesByFilm);
    }
}