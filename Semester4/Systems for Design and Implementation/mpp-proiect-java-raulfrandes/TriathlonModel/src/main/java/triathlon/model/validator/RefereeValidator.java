package triathlon.model.validator;


import triathlon.model.Referee;

public class RefereeValidator implements Validator<Referee> {
    @Override
    public void validate(Referee entity) throws ValidatorException {
        String errorMess = "";
        if (entity.getName().isEmpty()) {
            errorMess += "Invalid name - cannot be null\n";
        }
        if (entity.getUsername().isEmpty()) {
            errorMess += "Invalid name - cannot be null\n";
        }
        if (entity.getPassword().isEmpty()) {
            errorMess += "Invalid password - cannot be null\n";
        }
        if (entity.getPassword().length() < 4) {
            errorMess += "Invalid password - must have at least 4 characters";
        }
        if (!errorMess.isEmpty()){
            throw new ValidatorException(errorMess);
        }
    }
}
