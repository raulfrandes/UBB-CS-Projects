package com.example.lab_8.repository;

import com.example.lab_8.config.DatabaseConnectionConfig;
import com.example.lab_8.domain.User;
import com.example.lab_8.domain.validators.Validator;
import com.example.lab_8.repository.paging.Page;
import com.example.lab_8.repository.paging.PagingRepository;
import com.example.lab_8.repository.paging.Pegeable;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class UserDBRepository implements PagingRepository<Long, User> {

    private final Validator<User> validator;

    public UserDBRepository(Validator<User> validator){
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
    public Optional<User> findOne(Long aLong) {
        if (aLong == null){
            throw new RepositoryException("id cannot be null");
        }
        String statement = "select * from users where id = ?";
        try(PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(username, password, firstName, lastName);
                user.setId(userId);
                user.setSalt(salt);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<User> findAll() {
        HashSet<User> set = new HashSet<>();
        String statement = "select * from users;";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(username, password, firstName, lastName);
                user.setId(userId);
                user.setSalt(salt);
                set.add(user);
            }
            return set;
        } catch (SQLException e){
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> save(User entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "insert into users (first_name, last_name, username, password, salt) values (?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = getStatement(statement, Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getSalt());
            if (preparedStatement.executeUpdate() > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    Long generatedId = resultSet.getLong(1);
                    entity.setId(generatedId);
                }
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> delete(Long aLong) {
        if (aLong == null) {
            throw new RepositoryException("id cannot be null");
        }
        String statement = "delete from users where id=?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, aLong);
            Optional<User> deletedUser = findOne(aLong);
            if (preparedStatement.executeUpdate() > 0){
                return deletedUser;
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<User> update(User entity) {
        if (entity == null){
            throw new RepositoryException("entity cannot be null");
        }
        validator.validate(entity);
        String statement = "update users set first_name = ?, last_name = ?, username = ?, password = ?, salt = ? where id = ?";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getUsername());
            preparedStatement.setString(4, entity.getPassword());
            preparedStatement.setString(5, entity.getSalt());
            preparedStatement.setLong(6, entity.getId());
            if (preparedStatement.executeUpdate() > 0){
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private int returnNumberOfElements() {
        int numberOfElements = 0;
        String statement = "select count(*) as count from users;";
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
    public Page<User> findAll(Pegeable pegeable) {
        int numberOfElements = returnNumberOfElements();
        int limit = pegeable.getPageSize();
        int offset = pegeable.getPageSize() * pegeable.getPageNumber();
        if (offset >= numberOfElements) {
            return new Page<>(new ArrayList<>(), numberOfElements);
        }
        List<User> users = new ArrayList<>();
        String statement = "select * from users limit ? offset ?";
        try (PreparedStatement preparedStatement = getStatement(statement)){
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                Long userId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String salt = resultSet.getString("salt");
                User user = new User(username, password, firstName, lastName);
                user.setId(userId);
                user.setSalt(salt);
                users.add(user);
            }
            return new Page<>(users, numberOfElements);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
