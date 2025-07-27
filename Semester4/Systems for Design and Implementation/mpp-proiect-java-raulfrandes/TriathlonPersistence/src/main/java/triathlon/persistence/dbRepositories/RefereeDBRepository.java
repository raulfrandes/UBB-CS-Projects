package triathlon.persistence.dbRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Referee;
import triathlon.model.validator.Validator;
import triathlon.persistence.interfaces.IRefereeRepository;
import triathlon.persistence.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RefereeDBRepository implements IRefereeRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Referee> refereeValidator;

    public RefereeDBRepository(Properties props, Validator<Referee> refereeValidator) {
        logger.info("Initializing RefereeDBRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.refereeValidator = refereeValidator;
    }

    @Override
    public Referee findByUsername(String username) {
        if (username == null) {
            throw new RepositoryException("Username cannot be null");
        }
        logger.traceEntry("searching referee with username {}", username);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from referees where username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                Referee referee = new Referee(name, username, password);
                referee.setId(id);
                logger.traceExit(referee);
                return referee;
            }
            logger.traceExit(null);
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Iterable<Referee> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Referee> referees = new ArrayList<>();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from referees")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Referee referee = new Referee(name, username, password);
                referee.setId(id);
                referees.add(referee);
            }
            logger.traceExit(referees);
            return referees;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Referee findById(Long entityId) {
        if (entityId == null) {
            throw new RepositoryException("Id cannot be null");
        }
        logger.traceEntry("searching referee with id {}", entityId);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from referees where id = ?")) {
            preparedStatement.setLong(1, entityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Referee referee = new Referee(name, username, password);
                referee.setId(entityId);
                logger.traceExit(referee);
                return referee;
            }
            logger.traceExit(null);
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Referee save(Referee entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        refereeValidator.validate(entity);
        logger.traceEntry("saving referee {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("insert into referees (name, username, password) values (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getUsername());
            preparedStatement.setString(3, entity.getPassword());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                entity.setId(id);
                logger.traceExit(entity);
                return entity;
            }

            logger.traceExit(null);
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Referee deleteById(Long entityId) {
        return null;
    }

    @Override
    public Referee update(Referee entity) {
        return null;
    }
}
