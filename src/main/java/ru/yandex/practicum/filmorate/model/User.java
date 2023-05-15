package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private final Set<Integer> friends = new HashSet<>();

    public Set<Integer> getFriends() {
        return friends;
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }
}