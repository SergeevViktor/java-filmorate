package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}