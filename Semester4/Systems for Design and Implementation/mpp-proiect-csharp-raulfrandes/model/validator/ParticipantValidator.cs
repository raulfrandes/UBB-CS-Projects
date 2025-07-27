namespace model.validator
{
    public class ParticipantValidator : IValidator<Participant>
    {
        public void Validate(Participant entity)
        {
            string errorMess = "";
            if (string.IsNullOrEmpty(entity.Name))
            {
                errorMess += "Invalid name - cannot be null\n";
            }

            int codeI = int.Parse(entity.Code);
            if (codeI < 1000 || codeI > 9999)
            {
                errorMess += "Invalid code - must be between 1000 or 9999";
            }
            if (!string.IsNullOrEmpty(errorMess))
            {
                throw new ValidatorException(errorMess);
            }
        }
    }
}