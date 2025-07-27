package triathlon.persistence.hibernateRepositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import triathlon.model.Result;
import triathlon.model.validator.Validator;
import triathlon.persistence.dbRepositories.JdbcUtils;
import triathlon.persistence.interfaces.IResultRepository;

import java.util.List;
import java.util.Properties;

public class ResultHibernateRepository implements IResultRepository {
    private static final Logger logger = LogManager.getLogger();
    private final Validator<Result> resultValidator;

    public ResultHibernateRepository(Validator<Result> resultValidator) {
        this.resultValidator = resultValidator;
    }
    @Override
    public Iterable<Result> findAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from Result ", Result.class).getResultList();
        }
    }

    @Override
    public Result findById(Long entityId) {
        return null;
    }

    @Override
    public Result save(Result entity) {
        resultValidator.validate(entity);
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(entity));
        return entity;
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
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Result where participant.id=:id", Result.class)
                    .setParameter("id", idParticipant)
                    .getResultList();
        }
    }

    @Override
    public List<Result> findByTrial(Long idTrial) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createSelectionQuery("from Result where trial.id=:id", Result.class)
                    .setParameter("id", idTrial)
                    .getResultList();
        }
    }
}
