package ru.yandex.practicum.filmorate.storage.friendship;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.DbStorage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class FriendshipDbStorage extends DbStorage implements FriendshipStorage {

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Friendship added(Friendship friendship) {
        String sql = "INSERT INTO Friendship (user_id, friend_id, status) " +
                     "VALUES(?, ?, ?)";
        jdbcTemplate.update(sql,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.isStatus());
        return friendship;
    }

    @Override
    public Friendship update(Friendship friendship) {
        String updateSql = "UPDATE Friendship SET status = ? " +
                                            "WHERE user_id = ? " +
                                            "AND friend_id = ?";
        if (jdbcTemplate.update(updateSql,
                friendship.isStatus(),
                friendship.getUserId(),
                friendship.getFriendId()
        ) <= 0) {
            throw new ObjectNotFoundException("запись не найдена");
        } else {
            return friendship;
        }
    }

    @Override
    public boolean deleteById(Friendship friendship) {
        String sql = "DELETE FROM Friendship WHERE user_id = ? " +
                                            "AND friend_id = ?";
        return (jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId()) > 0);
    }

    @Override
    public List<Friendship> getFriendsIdByUser(int id) {
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT user_id," +
                            "friend_id," +
                            "status " +
                     "FROM Friendship " +
                     "WHERE user_id = ? " +
                     "OR (friend_id = ? " +
                     "AND status = ?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id, id, true);
        while (sqlRowSet.next()) {
            Friendship friendship = mapToRow(sqlRowSet);
            friendships.add(friendship);
        }
        return friendships;
    }


    @Override
    public Friendship getFriendsRelation(int userId1, int userId2) {

        String sqlQuery = "SELECT user_id," +
                                 "friend_id," +
                                 "status " +
                          "FROM Friendship " +
                          "WHERE user_id = ? " +
                          "AND friend_id = ? " +
                          "OR user_id = ? " +
                          "AND friend_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, friendshipRowMapper, userId1, userId2, userId2, userId1);
        } catch (Exception e) {
            return null;
        }
    }

    private final RowMapper<Friendship> friendshipRowMapper = (ResultSet resultSet, int rowNum) -> Friendship.builder()
            .userId(resultSet.getInt("user_id"))
            .friendId(resultSet.getInt("friend_id"))
            .status(resultSet.getBoolean("status"))
            .build();

    private Friendship mapToRow(SqlRowSet sqlRowSet) {
        int userId = sqlRowSet.getInt("user_id");
        int friendId = sqlRowSet.getInt("friend_id");
        boolean status = sqlRowSet.getBoolean("status");

        return Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .status(status)
                .build();
    }
}
