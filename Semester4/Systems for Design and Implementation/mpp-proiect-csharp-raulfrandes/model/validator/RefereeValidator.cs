namespace model.validator;

public class RefereeValidator: IValidator<Referee>
{
    public void Validate(Referee entity)
    {
        string errorMess = "";
        if (string.IsNullOrEmpty(entity.Name))
        {
            errorMess += "Invalid name - cannot be null\n";
        }
        if (string.IsNullOrEmpty(entity.Username))
        {
            errorMess += "Invalid name - cannot be null\n";
        }
        if (string.IsNullOrEmpty(entity.Password))
        {
            errorMess += "Invalid password - cannot be null\n";
        }
        if (entity.Password.Length < 4)
        {
            errorMess += "Invalid password - must have at least 4 characters";
        }
        if (!string.IsNullOrEmpty(errorMess))
        {
            throw new ValidatorException(errorMess);
        }
    }
}