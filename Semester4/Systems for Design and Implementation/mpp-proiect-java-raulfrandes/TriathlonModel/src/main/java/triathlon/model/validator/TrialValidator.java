package triathlon.model.validator;

import org.springframework.stereotype.Component;
import triathlon.model.Trial;

@Component
public class TrialValidator implements Validator<Trial>{
    @Override
    public void validate(Trial entity) throws ValidatorException {
        String errorMess = "";
        if (entity.getName().isEmpty()) {
            errorMess += "Invalid name - cannot be null";
        }
        if (entity.getDescription().isEmpty()) {
            errorMess += "Invalid name - cannot be null";
        }
        if (entity.getReferee() == null) {
            errorMess += "Invalid referee - cannot be null";
        }
        if (!errorMess.isEmpty()) {
            throw new ValidatorException(errorMess);
        }
    }
}
