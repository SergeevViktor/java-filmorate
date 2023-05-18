package ru.yandex.practicum.filmorate.service.userService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.ValidationService.validateUser;

@Slf4j
@Component
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, FriendshipDbStorage friendshipDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        userStorage.save(user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getListUser();
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        var friendshipsByUser1 = friendshipDbStorage.getFriendsIdByUser(userId1);
        var friendshipsByUser2 = friendshipDbStorage.getFriendsIdByUser(userId2);
        List<Integer> friendIdByUser1 = new ArrayList<>();
        List<Integer> friendIdByUser2 = new ArrayList<>();
        for (var friendshipsByUser : friendshipsByUser1) {
            int userId;
            if (friendshipsByUser.getUserId() == userId1)
                userId = friendshipsByUser.getFriendId();
            else
                userId = friendshipsByUser.getUserId();
            friendIdByUser1.add(userId);
        }
        for (var friendshipsByUser : friendshipsByUser2) {
            int userId;
            if (friendshipsByUser.getUserId() == userId2)
                userId = friendshipsByUser.getFriendId();
            else
                userId = friendshipsByUser.getUserId();
            friendIdByUser2.add(userId);
        }

        friendIdByUser1.retainAll(friendIdByUser2);
        List<User> users = new ArrayList<>();
        for (var commonFriendId : friendIdByUser1) {
            users.add(userStorage.getUserById(commonFriendId));
        }
        return users;
    }

    @Override
    public Friendship addFriend(int userId, int friendId) {
        var friendShip = friendshipDbStorage.getFriendsRelation(userId, friendId);
        if (friendShip == null) {
            try {
                return friendshipDbStorage.added(new Friendship(userId, friendId, false));
            } catch (Exception e) {
                throw new ObjectNotFoundException("Запись не найдена");
            }
        } else if (friendShip.isStatus()) {
            return friendShip;
        } else if (friendShip.getFriendId() == userId) {
            friendShip.setStatus(true);
            return friendshipDbStorage.update(friendShip);
        }
        return friendShip;
    }

    @Override
    public void deleteFriendById(int userId, int friendId) {
        var friendShip = friendshipDbStorage.getFriendsRelation(userId, friendId);
        if (friendShip == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            if (friendShip.isStatus()) {
                if (friendShip.getUserId() == userId) {
                    friendShip.setUserId(friendId);
                    friendShip.setFriendId(userId);
                }
                friendShip.setStatus(false);
                friendshipDbStorage.update(friendShip);
            } else {
                if (friendShip.getUserId() == userId) {
                    friendshipDbStorage.deleteById(friendShip);
                }
            }
        }
    }

    @Override
    public List<User> getListOfFriends(int id) {
        var friendships = friendshipDbStorage.getFriendsIdByUser(id);
        List<User> users = new ArrayList<>();
        for (var friendship : friendships) {
            int userId;
            if (friendship.getUserId() == id)
                userId = friendship.getFriendId();
            else
                userId = friendship.getUserId();
            var user = userStorage.getUserById(userId);
            users.add(user);
        }
        return users;
    }

    @Override
    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
}