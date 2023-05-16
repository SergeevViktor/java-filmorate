package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    Friendship added(Friendship friendship);

    Friendship update(Friendship friendship);

    boolean deleteById(Friendship friendship);

    List<Friendship> getFriendsIdByUser(int id);

    Friendship getFriendsRelation(int userId1, int userId2);
}