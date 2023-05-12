package ru.yandex.practicum.filmorate.service.userService;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(User user);
    List<User> getUsers();
    User getUserById(int id);
    List<User> getCommonFriends(int userId, int friendId);
    Integer addFriend(int userId, int friendId);
    void deleteFriendById(int userId, int friendId);
    List<User> getListOfFriends(int id);
}