package triathlon.persistence;

public interface IRepository<E, T> {
    Iterable<E> findAll();
    E findById(T entityId);
    E save(E entity);
    E deleteById(T entityId);
    E update(E entity);
}
