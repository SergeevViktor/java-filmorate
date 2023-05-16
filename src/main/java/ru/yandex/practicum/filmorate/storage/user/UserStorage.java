package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> getListUser();

    User getUserById(int id);

    User save(User user);

    User addUser(User user);

    void deleteAllUsers();
}