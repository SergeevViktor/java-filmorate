package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен GET-запрос - /users.");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен POST-запрос - /users.");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Для пользователя было измененно поле [name] на {}.", user.getLogin());
        }
        users.put(getNextId(), user);
        user.setId(id);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен PUT-запрос - /users.");
        if (users.containsKey(user.getId())) {
            log.info("Данные пользователя обновлены.");
            users.put(user.getId(), user);
        } else {
            log.warn("Пользователя с ID-{} не найдено.", user.getId());
            throw new ValidationException("Пользователя с данным id не найдено.");
        }
        return user;
    }

    public int getNextId() {
        id++;
        return id;
    }
}