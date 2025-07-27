namespace model.validator;

public interface IValidator<T>
{
    void Validate(T entity);
}