package com.example.lab_8.repository;

import com.example.lab_8.config.DatabaseConnectionConfig;
import com.example.lab_8.domain.*;
import com.example.lab_8.domain.validators.Validator;
import com.example.lab_8.repository.paging.Page;
import com.example.lab_8.repository.paging.PagingRepository;
import com.example.lab_8.repository.paging.Pegeable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class FriendshipDBRepository implements PagingFriendshipRepository {
    private Validator<Friendship> validator;
    public FriendshipDBRepository(Validator<Friendship> validator){
        this.validator = validator;
    }

    private PreparedStatement getStatement(String statement) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement);
    }

    private PreparedStatement getStatement(String statement, int property) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement, property);
    }

    @Override
    public Optional<Friendship> findOne(Tuple<Long, Long> longLongTuple) {
        if (longLongTuple == null){
            throw new RepositoryException("id cannot be null");
        }
        String statement = "SELECT f.iduser1, " +
                "       f.iduser2, " +
                "       f.friendsfrom, " +
                "       f.status, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username_u1, " +
                "       u1.password AS password_u1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username_u2, " +
                "       u2.password AS password_u2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.iduser1 = u1.id " +
                "LEFT JOIN users u2 ON f.iduser2 = u2.id " +
                "WHERE f.iduser1 = ? AND f.iduser2 = ?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, longLongTuple.getLeft());
            preparedStatement.setLong(2, longLongTuple.getRight());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                Long user1Id = resultSet.getLong("iduser1");
                Long user2Id = resultSet.getLong("iduser2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                FriendRequest status = FriendRequest.valueOf(resultSet.getString("status"));
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String username_u1 = resultSet.getString("username_u1");
                String password_u1 = resultSet.getString("password_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username_u2 = resultSet.getString("username_u2");
                String password_u2 = resultSet.getString("password_u2");
                User u1 = new User(username_u1, password_u1, first_name_u1, last_name_u1);
                u1.setId(user1Id);
                User u2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                u2.setId(user2Id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom, status);
                Tuple<Long, Long> friendId = new Tuple<>(user1Id, user2Id);
                friendship.setId(friendId);
                return Optional.of(friendship);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Friendship> findAll() {
        HashSet<Friendship> set = new HashSet<>();
        String statement = "SELECT f.iduser1, " +
                           "       f.iduser2, " +
                           "       f.friendsfrom, " +
                           "       f.status, " +
                           "       u1.first_name AS first_name_u1, " +
                           "       u1.last_name AS last_name_u1, " +
                           "       u1.username AS username_u1, " +
                           "       u1.password AS password_u1, " +
                           "       u2.first_name AS first_name_u2, " +
                           "       u2.last_name AS last_name_u2, " +
                           "       u2.username AS username_u2, " +
                           "       u2.password AS password_u2 " +
                           "FROM friendships f " +
                           "LEFT JOIN users u1 ON f.iduser1 = u1.id " +
                           "LEFT JOIN users u2 ON f.iduser2 = u2.id;";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long user1Id = resultSet.getLong("iduser1");
                Long user2Id = resultSet.getLong("iduser2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                FriendRequest status = FriendRequest.valueOf(resultSet.getString("status"));
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String username_u1 = resultSet.getString("username_u1");
                String password_u1 = resultSet.getString("password_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username_u2 = resultSet.getString("username_u2");
                String password_u2 = resultSet.getString("password_u2");
                User u1 = new User(username_u1, password_u1, first_name_u1, last_name_u1);
                u1.setId(user1Id);
                User u2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                u2.setId(user2Id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom, status);
                Tuple<Long, Long> friendId = new Tuple<>(user1Id, user2Id);
                friendship.setId(friendId);
                set.add(friendship);
            }
            return set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "insert into friendships (iduser1, iduser2, friendsFrom, status) " +
                           "values (?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1, entity.getId().getLeft());
            preparedStatement.setLong(2, entity.getId().getRight());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getFriendsFrom()));
            preparedStatement.setString(4, entity.getStatus().toString());
            if (preparedStatement.executeUpdate() > 0){
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> delete(Tuple<Long, Long> longLongTuple) {
        if (longLongTuple == null){
            throw new RepositoryException("id cannot be null");
        }
        String statement = "delete from friendships where iduser1=? and iduser2=?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, longLongTuple.getLeft());
            preparedStatement.setLong(2, longLongTuple.getRight());
            Optional<Friendship> deletedFriendship = findOne(longLongTuple);
            if (preparedStatement.executeUpdate() > 0){
                return deletedFriendship;
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "update friendships " +
                           "set status = ? " +
                           "where iduser1 = ? AND iduser2 = ?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setString(1, entity.getStatus().toString());
            preparedStatement.setLong(2, entity.getId().getLeft());
            preparedStatement.setLong(3, entity.getId().getRight());
            if (preparedStatement.executeUpdate() > 0){
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int returnNumberOfElements(User user) {
        int numberOfElements = 0;
        String statement = "select count(*) as count from friendships f " +
                "LEFT JOIN users u1 ON f.iduser1 = u1.id LEFT JOIN users u2 ON f.iduser2 = u2.id ";
        if (user != null) {
            statement += "WHERE u1.id = " + user.getId() + " OR u2.id = " + user.getId() + " ";
        }
        try (PreparedStatement preparedStatement = getStatement(statement)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                numberOfElements = resultSet.getInt("count");
            }
            return numberOfElements;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Page<Friendship> findAll(Pegeable pegeable, User user) {
        int numberOfElements = returnNumberOfElements(user);
        int limit = pegeable.getPageSize();
        int offset = pegeable.getPageSize() * pegeable.getPageNumber();
        if (offset >= numberOfElements) {
            return new Page<>(new ArrayList<>(), numberOfElements);
        }
        List<Friendship> friendships = new ArrayList<>();
        String statement = "SELECT f.iduser1, " +
                "       f.iduser2, " +
                "       f.friendsfrom, " +
                "       f.status, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username_u1, " +
                "       u1.password AS password_u1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username_u2, " +
                "       u2.password AS password_u2 " +
                "FROM friendships f " +
                "LEFT JOIN users u1 ON f.iduser1 = u1.id " +
                "LEFT JOIN users u2 ON f.iduser2 = u2.id ";
        if (user != null) {
            statement += "WHERE u1.id = " + user.getId() + " OR u2.id = " + user.getId() + " ";
        }
        statement += "LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long user1Id = resultSet.getLong("iduser1");
                Long user2Id = resultSet.getLong("iduser2");
                LocalDateTime friendsFrom = resultSet.getTimestamp("friendsFrom").toLocalDateTime();
                FriendRequest status = FriendRequest.valueOf(resultSet.getString("status"));
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String username_u1 = resultSet.getString("username_u1");
                String password_u1 = resultSet.getString("password_u1");
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username_u2 = resultSet.getString("username_u2");
                String password_u2 = resultSet.getString("password_u2");
                User u1 = new User(username_u1, password_u1, first_name_u1, last_name_u1);
                u1.setId(user1Id);
                User u2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                u2.setId(user2Id);
                Friendship friendship = new Friendship(u1, u2, friendsFrom, status);
                Tuple<Long, Long> friendId = new Tuple<>(user1Id, user2Id);
                friendship.setId(friendId);
                friendships.add(friendship);
            }
            return new Page<>(friendships, numberOfElements);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Page<Friendship> findAll(Pegeable pegeable) {
        return findAll(pegeable, null);
    }
}
