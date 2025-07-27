package triathlon.persistence.dbRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Referee;
import triathlon.model.Trial;
import triathlon.model.validator.Validator;
import triathlon.persistence.interfaces.ITrialRepository;
import triathlon.persistence.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TrialDBRepository implements ITrialRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Trial> trialValidator;

    public TrialDBRepository(Properties props, Validator<Trial> trialValidator) {
        logger.info("Initializing TrialDBRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.trialValidator = trialValidator;
    }

    private Referee findReferee(Long idReferee) {
        if (idReferee == null) {
            throw new RepositoryException("Referee cannot be null");
        }
        logger.traceEntry("searching referee with id {}", idReferee);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from referees where id = ?")) {
            preparedStatement.setLong(1, idReferee);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                Referee referee = new Referee(name, username, password);
                referee.setId(idReferee);
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
    public Iterable<Trial> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Trial> trials = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("select * from trials")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Long idReferee = resultSet.getLong("id_referee");
                Referee referee = findReferee(idReferee);
                Trial trial = new Trial(name, description, referee);
                trial.setId(id);
                trials.add(trial);
            }
            logger.traceExit(trials);
            return trials;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Trial findById(Long entityId) {
        if (entityId == null) {
            throw new RepositoryException("Id cannot be null");
        }
        logger.traceEntry("searching trial with id {}", entityId);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from trials where id = ?")) {
            preparedStatement.setLong(1, entityId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Long idReferee = resultSet.getLong("id_referee");
                Referee referee = findReferee(idReferee);
                Trial trial = new Trial(name, description, referee);
                trial.setId(id);
                logger.traceExit(referee);
                return trial;
            }
            logger.traceExit(null);
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Trial save(Trial entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        trialValidator.validate(entity);
        logger.traceEntry("saving trial {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("insert into trials (name, description, id_referee) values (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setLong(3, entity.getReferee().getId());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {} instances", result);

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                entity.setId(id);
                logger.traceExit(entity);
                return entity;
            }

            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Trial deleteById(Long entityId) {
        return null;
    }

    @Override
    public Trial update(Trial entity) {
        return null;
    }
}
