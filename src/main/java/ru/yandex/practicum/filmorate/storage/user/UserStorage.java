package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();
    List<User> getAll();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(User user);

    User getById(int id);
}
