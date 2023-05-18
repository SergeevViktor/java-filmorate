package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.*;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private Set<Integer> likes;
    private List<Genre> genres;
    @NotNull
    private RatingMpa mpa;
}