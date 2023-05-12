package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    Map<Integer, User> getUsers();
    List<User> getListUser();
    User getUserById(int id);
    User save(User user);
    User addUser(User user);
    void deleteAllUsers();
}