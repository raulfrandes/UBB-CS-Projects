package triathlon.persistence.hibernateRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import triathlon.model.Referee;
import triathlon.model.Trial;
import triathlon.model.validator.Validator;
import triathlon.persistence.dbRepositories.JdbcUtils;
import triathlon.persistence.interfaces.IRefereeRepository;

import java.util.Properties;

public class RefereeHibernateRepository implements IRefereeRepository {

    private static final Logger logger = LogManager.getLogger();
    private final Validator<Referee> refereeValidator;

    public RefereeHibernateRepository(Validator<Referee> refereeValidator) {
        this.refereeValidator = refereeValidator;
    }

    @Override
    public Iterable<Referee> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Referee", Referee.class).getResultList();
        }
    }

    @Override
    public Referee findById(Long entityId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Referee where id=:id", Referee.class)
                    .setParameter("id", entityId)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Referee save(Referee entity) {
        final Referee[] addedReferee = {new Referee()};
        refereeValidator.validate(entity);
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                addedReferee[0] = entity;
            } else {
                addedReferee[0] = session.merge(entity);
            }
        });
        return addedReferee[0];
    }

    @Override
    public Referee deleteById(Long entityId) {
        return null;
    }

    @Override
    public Referee update(Referee entity) {
        return null;
    }

    @Override
    public Referee findByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Referee where username=:usernameParam", Referee.class)
                    .setParameter("usernameParam", username)
                    .getSingleResultOrNull();
        }
    }
}
