namespace model.validator;

public class ResultValidator : IValidator<Result>
{
    public void Validate(Result entity)
    {
        string errorMessage = "";
        if (entity.Points < 0)
        {
            errorMessage += "Invalid points - cannot be less than 0\n";
        }
        if (!string.IsNullOrEmpty(errorMessage))
        {
            throw new ValidatorException(errorMessage);
        }
    }
}