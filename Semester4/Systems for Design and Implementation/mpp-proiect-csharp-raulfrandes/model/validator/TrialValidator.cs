namespace model.validator
{
    public class TrialValidator : IValidator<Trial>
    {
        public void Validate(Trial entity)
        {
            string errorMess = "";
            if (string.IsNullOrEmpty(entity.Name))
            {
                errorMess += "Invalid name - cannot be null\n";
            }

            if (string.IsNullOrEmpty(entity.Description))
            {
                errorMess += "Invalid description - cannot be null\n";
            }

            if (!string.IsNullOrEmpty(errorMess))
            {
                throw new ValidatorException(errorMess);
            }
        }
    }
}