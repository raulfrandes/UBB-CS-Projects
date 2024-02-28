package com.example.lab_8.repository;

import com.example.lab_8.config.DatabaseConnectionConfig;
import com.example.lab_8.domain.Message;
import com.example.lab_8.domain.ReplyMessage;
import com.example.lab_8.domain.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository implements Repository<Long, Message> {
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

    private PreparedStatement getStatement(String statement, int property1, int property2) throws SQLException {
        Connection connection = DriverManager.getConnection(DatabaseConnectionConfig.DB_URL,
                DatabaseConnectionConfig.DB_USER, DatabaseConnectionConfig.DB_PASSWORD);
        return connection.prepareStatement(statement, property1, property2);
    }
    @Override
    public Optional<Message> findOne(Long aLong) {
        if (aLong == null) {
            throw new RepositoryException("id cannot be null");
        }
        String statement = "SELECT m.id, " +
                           "       m.id_source, " +
                           "       mr.id_destination, " +
                           "       m.message, " +
                           "       m.date, " +
                           "       m.reply, " +
                           "       u1.first_name AS first_name_u1, " +
                           "       u1.last_name AS last_name_u1, " +
                           "       u1.username AS username_u1, " +
                           "       u1.password AS password_u1, " +
                           "       u2.first_name AS first_name_u2, " +
                           "       u2.last_name AS last_name_u2, " +
                           "       u2.username AS username_u2, " +
                           "       u2.password AS password_u2 " +
                           "FROM messages m " +
                           "INNER JOIN messageReceivers mr ON m.id = mr.id_message " +
                           "LEFT JOIN users u1 ON m.id_source = u1.id " +
                           "LEFT JOIN users u2 ON mr.id_destination = u2.id " +
                           "WHERE m.id = ?;";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> receivers = new ArrayList<>();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idSource = resultSet.getLong("id_source");
                Long idDestination = resultSet.getLong("id_destination");
                String mess = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                Long reply = resultSet.getLong("reply");
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String username_u1 = resultSet.getString("username_u1");
                String password_u1 = resultSet.getString("password_u1");
                User user1 = new User(username_u1, password_u1, first_name_u1, last_name_u1);
                user1.setId(idSource);
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username_u2 = resultSet.getString("username_u2");
                String password_u2 = resultSet.getString("password_u2");
                User user2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                user2.setId(idDestination);
                receivers.add(user2);
                while (resultSet.next()) {
                    idDestination = resultSet.getLong("id_destination");
                    first_name_u2 = resultSet.getString("first_name_u2");
                    last_name_u2 = resultSet.getString("last_name_u2");
                    username_u2 = resultSet.getString("username_u2");
                    password_u2 = resultSet.getString("password_u2");
                    user2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                    user2.setId(idDestination);
                    receivers.add(user2);
                }
                if (reply.equals(0L)) {
                    Message message = new Message(user1, receivers, mess, date);
                    message.setId(id);
                    return Optional.of(message);
                }
                else {
                    ReplyMessage message = new ReplyMessage(user1, receivers, mess, date, findOne(reply).get());
                    message.setId(id);
                    return Optional.of(message);
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Iterable<Message> findAll() {
        HashSet<Message> set = new HashSet<>();
        String statement = "SELECT m.id, " +
                "       m.id_source, " +
                "       mr.id_destination, " +
                "       m.message, " +
                "       m.date, " +
                "       m.reply, " +
                "       u1.first_name AS first_name_u1, " +
                "       u1.last_name AS last_name_u1, " +
                "       u1.username AS username_u1, " +
                "       u1.password AS password_u1, " +
                "       u2.first_name AS first_name_u2, " +
                "       u2.last_name AS last_name_u2, " +
                "       u2.username AS username_u2, " +
                "       u2.password AS password_u2 " +
                "FROM messages m " +
                "INNER JOIN messageReceivers mr ON m.id = mr.id_message " +
                "LEFT JOIN users u1 ON m.id_source = u1.id " +
                "LEFT JOIN users u2 ON mr.id_destination = u2.id " +
                "ORDER BY m.id DESC " +
                "LIMIT 5";
        try (PreparedStatement preparedStatement = getStatement(statement, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY)){
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> receivers = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idSource = resultSet.getLong("id_source");
                Long idDestination = resultSet.getLong("id_destination");
                String mess = resultSet.getString("message");
                LocalDateTime date = resultSet.getTimestamp("date").toLocalDateTime();
                String first_name_u1 = resultSet.getString("first_name_u1");
                String last_name_u1 = resultSet.getString("last_name_u1");
                String username_u1 = resultSet.getString("username_u1");
                String password_u1 = resultSet.getString("password_u1");
                User user1 = new User(username_u1, password_u1, first_name_u1, last_name_u1);
                user1.setId(idSource);
                String first_name_u2 = resultSet.getString("first_name_u2");
                String last_name_u2 = resultSet.getString("last_name_u2");
                String username_u2 = resultSet.getString("username_u2");
                String password_u2 = resultSet.getString("password_u2");
                User user2 = new User(username_u2, password_u2, first_name_u2, last_name_u2);
                user2.setId(idDestination);
                receivers.add(user2);
                Long reply = resultSet.getLong("reply");
                if (resultSet.next()){
                    if (resultSet.getLong("id") != id) {
                        if (reply.equals(0L)) {
                            Message message = new Message(user1, new ArrayList<>(receivers), mess, date);
                            message.setId(id);
                            set.add(message);
                            receivers.clear();
                        }
                        else {
                            ReplyMessage message = new ReplyMessage(user1, new ArrayList<>(receivers), mess, date, findOne(reply).get());
                            message.setId(id);
                            set.add(message);
                            receivers.clear();
                        }
                    }
                }
                else {
                    if (reply.equals(0L)) {
                        Message message = new Message(user1, receivers, mess, date);
                        message.setId(id);
                        set.add(message);
                    }
                    else {
                        ReplyMessage message = new ReplyMessage(user1, receivers, mess, date, findOne(reply).get());
                        message.setId(id);
                        set.add(message);
                    }
                }
                resultSet.previous();
            }
            return  set;
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    private void saveReceivers(Long id_message, Long id_destination) {
        String statement = "insert into messageReceivers (id_message, id_destination) values (?, ?);";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, id_message);
            preparedStatement.setLong(2, id_destination);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Message> save(Message entity) {
        if (entity == null) {
            throw new RepositoryException("entity cannot be null");
        }
        if (entity instanceof ReplyMessage) {
            String statement = "insert into messages (id_source, message, date, reply) values (?, ?, ?, ?);";
            try (PreparedStatement preparedStatement = getStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, entity.getFrom().getId());
                preparedStatement.setString(2, entity.getMessage());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
                preparedStatement.setLong(4, ((ReplyMessage) entity).getReplyTo().getId());
                if (preparedStatement.executeUpdate() > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        Long generatedId = resultSet.getLong(1);
                        entity.setId(generatedId);
                    }
                    List<User> receivers = entity.getTo();
                    receivers.forEach(user -> saveReceivers(entity.getId(), user.getId()));
                    return Optional.empty();
                }
                return Optional.of(entity);
            } catch (SQLException e) {
                throw new RepositoryException(e);
            }
        }
        else {
            String statement = "insert into messages (id_source, message, date) values (?, ?, ?);";
            try (PreparedStatement preparedStatement = getStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setLong(1, entity.getFrom().getId());
                preparedStatement.setString(2, entity.getMessage());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(entity.getDate()));
                if (preparedStatement.executeUpdate() > 0) {
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        Long generatedId = resultSet.getLong(1);
                        entity.setId(generatedId);
                    }
                    List<User> receivers = entity.getTo();
                    receivers.forEach(user -> saveReceivers(entity.getId(), user.getId()));
                    return Optional.empty();
                }
                return Optional.of(entity);
            } catch (SQLException e) {
                throw new RepositoryException(e);
            }
        }
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        if (aLong == null) {
            throw new RepositoryException("id cannot be null");
        }
        String statement = "delete from messages where id = ?";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setLong(1, aLong);
            Optional<Message> deletedMessage = findOne(aLong);
            if (preparedStatement.executeUpdate() > 0) {
                return deletedMessage;
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }

    @Override
    public Optional<Message> update(Message entity) {
        if (entity == null) {
            throw new RepositoryException("entity cannot be null");
        }
        String statement = "update messages set message = ?, date = ? where id = ?";
        try (PreparedStatement preparedStatement = getStatement(statement)) {
            preparedStatement.setString(1, entity.getMessage());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getDate()));
            preparedStatement.setLong(3, entity.getId());
            if (preparedStatement.executeUpdate() > 0) {
                return Optional.empty();
            }
            return Optional.of(entity);
        } catch (SQLException e) {
            throw new RepositoryException(e);
        }
    }
}
