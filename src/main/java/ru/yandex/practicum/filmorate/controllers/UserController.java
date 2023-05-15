package ru.yandex.practicum.filmorate.controllers;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.userService.UserService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        log.info("Получен GET-запрос - /users.");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен GET-запрос - /users/{id}.");
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getListOfFriends(@PathVariable int id) {
        log.info("Получен GET-запрос - /users/{id}/friends.");
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен GET-запрос - /users/{id}/friends/common/{otherId}.");
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создан пользоваьель: {}.", user.getName());
        return userService.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Пользователь обновлен: {}.", user.getName());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь добавлен в друзья: {}", userService.getUserById(friendId).getName());
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователь был удален из списка друзей: {}", userService.getUserById(friendId).getName());
        userService.deleteFriendById(id, friendId);
    }
}