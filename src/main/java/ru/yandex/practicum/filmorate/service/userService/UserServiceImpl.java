package ru.yandex.practicum.filmorate.service.userService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Slf4j
@Component
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;

    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.getUsers().containsKey(user.getId())) {
            validateUser(user);
            userStorage.save(user);
            return user;
        } else {
            log.error("ERROR: ID-{} не найден!", user.getId());
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }

    }

    @Override
    public List<User> getUsers() {
        return userStorage.getListUser();
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        Set<Integer> users = userStorage.getUserById(userId).getFriends();
        return userStorage.getUserById(friendId).getFriends().stream()
                .filter(users::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Integer addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        return userStorage.getUserById(userId).getFriends().size();
    }

    @Override
    public void deleteFriendById(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.debug("Total friends: {}", userStorage.getUserById(userId).getFriends().size());
    }

    @Override
    public List<User> getListOfFriends(int id) {
        return userStorage.getUserById(id).getFriends().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}