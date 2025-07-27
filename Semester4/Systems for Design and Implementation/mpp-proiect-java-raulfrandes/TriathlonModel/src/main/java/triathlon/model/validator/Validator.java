package triathlon.model.validator;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
