package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (storage.getUsers().containsKey(userId) && storage.getUsers().containsKey(friendId)) {
            storage.getById(userId).addFriend(friendId);
            storage.getById(friendId).addFriend(userId);
        } else {
            throw new ValidationException("Пользователь не найден.");
        }
    }
}
