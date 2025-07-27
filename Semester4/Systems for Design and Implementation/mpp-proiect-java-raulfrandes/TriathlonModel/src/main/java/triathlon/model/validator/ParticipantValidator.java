package triathlon.model.validator;


import triathlon.model.Participant;

public class ParticipantValidator implements Validator<Participant> {
    @Override
    public void validate(Participant entity) throws ValidatorException {
        String errorMess = "";
        if (entity.getName().isEmpty()) {
            errorMess += "Invalid name - cannot be null";
        }
        int codeI = Integer.parseInt(entity.getCode());
        if (codeI < 1000 || codeI > 9999) {
            errorMess += "Invalid code - must be an number between 1000 and 9999";
        }
        if (!errorMess.isEmpty()) {
            throw new ValidatorException(errorMess);
        }
    }
}
