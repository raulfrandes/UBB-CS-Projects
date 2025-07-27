package triathlon.model.validator;

import triathlon.model.Result;

public class ResultValidator implements Validator<Result> {
    @Override
    public void validate(Result entity) throws ValidatorException {
        String errorMess = "";
        if (entity.getTrial() == null) {
            errorMess += "Invalid trial - cannot be null";
        }
        if (entity.getParticipant() == null) {
            errorMess += "Invalid participant - cannot be null";
        }
        if (entity.getPoints() < 0) {
            errorMess += "Invalid points - cannot be less than 0";
        }
        if (!errorMess.isEmpty()) {
            throw new ValidatorException(errorMess);
        }
    }
}
