package triathlon.persistence.hibernateRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import triathlon.model.Referee;
import triathlon.model.Trial;
import triathlon.model.validator.Validator;
import triathlon.persistence.dbRepositories.JdbcUtils;
import triathlon.persistence.interfaces.ITrialRepository;

import java.util.Objects;
import java.util.Properties;

@Component
public class TrialHibernateRepository implements ITrialRepository {

    private static final Logger logger = LogManager.getLogger();
    private final Validator<Trial> trialValidator;

    @Autowired
    public TrialHibernateRepository(Validator<Trial> trialValidator) {
        this.trialValidator = trialValidator;
    }

    @Override
    public Iterable<Trial> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Trial", Trial.class).getResultList();
        }
    }

    @Override
    public Trial findById(Long entityId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Trial where id=:id", Trial.class)
                    .setParameter("id", entityId)
                    .getSingleResultOrNull();
        }
    }

    @Override
    public Trial save(Trial entity) {
        final Trial[] addedTrial = {new Trial()};
        trialValidator.validate(entity);
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Referee referee = entity.getReferee();
            if (referee != null && referee.getId() == null) {
                session.merge(referee);
            }
            if (entity.getId() == null) {
                session.persist(entity);
                addedTrial[0] = entity;
            } else {
                addedTrial[0] = session.merge(entity);
            }
        });
        return addedTrial[0];
    }

    @Override
    public Trial deleteById(Long entityId) {
        final Trial[] deletedTrial = {new Trial()};
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Trial trial = session.createQuery("from Trial where id=?1", Trial.class)
                    .setParameter(1, entityId).uniqueResult();
            System.out.println("In delete: " + trial);
            if (trial != null) {
                session.remove(trial);
                session.flush();
                deletedTrial[0] = trial;
            }
        });
        return deletedTrial[0];
    }

    @Override
    public Trial update(Trial entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (!Objects.isNull(session.find(Trial.class, entity.getId()))) {
                System.out.println("In update: " + entity.getId());
                session.merge(entity);
                session.flush();
            }
        });
        return entity;
    }
}
