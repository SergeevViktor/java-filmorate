package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Для пользователя было измененно поле [name] на {}.", user.getLogin());
        }
        users.put(getNextId(), user);
        user.setId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Данные пользователя обновлены.");
            users.put(user.getId(), user);
        } else {
            log.warn("Пользователя с ID-{} не найдено.", user.getId());
            throw new ValidationException("Пользователя с данным id не найдено.");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())) {
            log.info("Данные пользователя удалены.");
            users.remove(user.getId());
        } else {
            log.warn("Пользователя с ID-{} не найдено.", user.getId());
            throw new ValidationException("Пользователя с данным id не найдено.");
        }
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    private int getNextId() {
        id++;
        return id;
    }
}
