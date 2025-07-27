package triathlon.persistence.dbRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.model.Trial;
import triathlon.model.validator.Validator;
import triathlon.persistence.interfaces.IResultRepository;
import triathlon.persistence.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ResultDBRepository implements IResultRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Result> resultValidator;

    public ResultDBRepository(Properties props, Validator<Result> resultValidator) {
        logger.info("Initializing ResultDBRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.resultValidator = resultValidator;
    }

    private Participant findParticipant(Long idParticipant) {
        if (idParticipant == null) {
            throw new RepositoryException("Participant cannot be null");
        }
        logger.traceEntry("searching participant with id {}", idParticipant);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from participants where id = ?")) {
            preparedStatement.setLong(1, idParticipant);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                Participant participant = new Participant(name, code);
                participant.setId(id);
                logger.traceExit(participant);
                return participant;
            }
            logger.traceExit(null);
            return null;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
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

    private Trial findTrial(Long idTrial) {
        if (idTrial == null) {
            throw new RepositoryException("Trial cannot be null");
        }
        logger.traceEntry("searching trial with id {}", idTrial);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from trials where id = ?")) {
            preparedStatement.setLong(1, idTrial);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Long idReferee = resultSet.getLong("id_referee");
                Referee referee = findReferee(idReferee);
                Trial trial = new Trial(name, description, referee);
                trial.setId(id);
                logger.traceExit(trial);
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
    public Iterable<Result> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Result> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("select * from results")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idParticipant = resultSet.getLong("id_participant");
                Participant participant = findParticipant(idParticipant);
                Long idTrial = resultSet.getLong("id_trial");
                Trial trial = findTrial(idTrial);
                Integer points = resultSet.getInt("points");
                Result result = new Result(participant, trial, points);
                result.setId(id);
                results.add(result);
            }
            logger.traceExit(results);
            return results;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Result findById(Long entityId) {
        return null;
    }

    @Override
    public Result save(Result entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        resultValidator.validate(entity);
        logger.traceEntry("saving result {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("insert into results (id_participant, id_trial, points) values (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getParticipant().getId());
            preparedStatement.setLong(2, entity.getTrial().getId());
            preparedStatement.setInt(3, entity.getPoints());
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
    public Result deleteById(Long entityId) {
        return null;
    }

    @Override
    public Result update(Result entity) {
        return null;
    }

    @Override
    public List<Result> findByParticipant(Long idParticipant) {
        if (idParticipant == null) {
            throw new RepositoryException("Participant cannot be null");
        }
        logger.traceEntry("searching results of participant {}", idParticipant);
        Connection connection = dbUtils.getConnection();
        List<Result> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("select * from results where id_participant = ?")){
            preparedStatement.setLong(1, idParticipant);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Participant participant = findParticipant(idParticipant);
                Long idTrial = resultSet.getLong("id_trial");
                Trial trial = findTrial(idTrial);
                Integer points = resultSet.getInt("points");
                Result result = new Result(participant, trial, points);
                result.setId(id);
                results.add(result);
            }
            logger.traceExit(results);
            return results;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public List<Result> findByTrial(Long idTrial) {
        if (idTrial == null) {
            throw new RepositoryException("Trial cannot be null");
        }
        logger.traceEntry("searching results of trial {}", idTrial);
        Connection connection = dbUtils.getConnection();
        List<Result> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("select * from results where id_trial = ?")){
            preparedStatement.setLong(1, idTrial);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long idParticipant = resultSet.getLong("id_participant");
                Participant participant = findParticipant(idParticipant);
                Trial trial = findTrial(idTrial);
                Integer points = resultSet.getInt("points");
                Result result = new Result(participant, trial, points);
                result.setId(id);
                results.add(result);
            }
            logger.traceExit(results);
            return results;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }
}
