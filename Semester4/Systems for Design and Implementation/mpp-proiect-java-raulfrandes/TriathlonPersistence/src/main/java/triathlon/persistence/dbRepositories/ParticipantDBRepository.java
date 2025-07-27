package triathlon.persistence.dbRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import triathlon.model.Participant;
import triathlon.model.validator.Validator;
import triathlon.persistence.interfaces.IParticipantRepository;
import triathlon.persistence.RepositoryException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParticipantDBRepository implements IParticipantRepository {

    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Participant> participantValidator;

    public ParticipantDBRepository(Properties props, Validator<Participant> participantValidator) {
        logger.info("Initializing ParticipantDBRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
        this.participantValidator = participantValidator;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection connection = dbUtils.getConnection();
        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("select * from participants")){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String code = resultSet.getString("code");
                Participant participant = new Participant(name, code);
                participant.setId(id);
                participants.add(participant);
            }
            logger.traceExit(participants);
            return participants;
        } catch (SQLException e) {
            logger.error(e);
            throw new RepositoryException("Error DB " + e);
        }
    }

    @Override
    public Participant findById(Long entityId) {
        if (entityId == null) {
            throw new RepositoryException("Id cannot be null");
        }
        logger.traceEntry("searching participant with id {}", entityId);
        Connection connection = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = connection
                .prepareStatement("select * from participants where id = ?")) {
            preparedStatement.setLong(1, entityId);
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

    @Override
    public Participant save(Participant entity) {
        if (entity == null) {
            throw new RepositoryException("Entity cannot be null");
        }
        participantValidator.validate(entity);
        logger.traceEntry("saving participant {}", entity);
        Connection connection = dbUtils.getConnection();
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("insert into participants (name, code) values (?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getCode());
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
    public Participant deleteById(Long entityId) {
        return null;
    }

    @Override
    public Participant update(Participant entity) {
        return null;
    }
}
