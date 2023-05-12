package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
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
    public List<User> getListUser() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(getNextId());
        return users.put(user.getId(), user);
    }

    @Override
    public User save(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            log.error("ERROR: ID-{} не найден!", id);
            throw new ObjectNotFoundException(String.format("User's id %d doesn't found!", id));
        }
        return users.get(id);
    }

    private int getNextId() {
        id++;
        return id;
    }
}
