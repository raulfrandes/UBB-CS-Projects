package triathlon.networking.protocol.dto;

import triathlon.model.Participant;
import triathlon.model.Result;
import triathlon.model.Trial;

public class DTOUtils {
    public static Result getFromDTO(ResultDTO resultDTO) {
        Participant participant = new Participant(null, null);
        participant.setId(resultDTO.getIdParticipant());
        Trial trial = new Trial(null, null, null);
        trial.setId(resultDTO.getIdTrial());
        return new Result(participant, trial, resultDTO.getPoints());
    }

    public static ResultDTO getDTO(Result result) {
        Long idParticipant = result.getParticipant().getId();
        Long idTrial = result.getTrial().getId();
        return new ResultDTO(idParticipant, idTrial, result.getPoints());
    }
}
