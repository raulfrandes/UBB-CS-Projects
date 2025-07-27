package triathlon.persistence.hibernateRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import triathlon.model.Participant;
import triathlon.model.validator.Validator;
import triathlon.persistence.dbRepositories.JdbcUtils;
import triathlon.persistence.interfaces.IParticipantRepository;

import java.util.Properties;

public class ParticipantHibernateRepository implements IParticipantRepository {
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Participant> participantValidator;

    public ParticipantHibernateRepository(Validator<Participant> participantValidator) {
        this.participantValidator = participantValidator;
    }
    @Override
    public Iterable<Participant> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Participant ", Participant.class).getResultList();
        }
    }

    @Override
    public Participant findById(Long entityId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Participant where id=:id", Participant.class)
                    .setParameter("id", entityId)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Participant save(Participant entity) {
        participantValidator.validate(entity);
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return entity;
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
