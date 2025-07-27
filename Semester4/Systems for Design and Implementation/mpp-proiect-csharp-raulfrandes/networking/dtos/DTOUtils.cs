using model;

namespace networking.dtos;

public class DTOUtils
{
    public static Result GetFromDTO(ResultDTO resultDto)
    {
        Participant participant = new Participant("", "");
        participant.Id = resultDto.IdParticipant;
        Trial trial = new Trial("", "", null!);
        trial.Id = resultDto.IdTrial;
        return new Result(participant, trial, resultDto.Points);
    }

    public static ResultDTO GetDTO(Result result)
    {
        long idParticipant = result.Participant.Id;
        long idTrial = result.Trial.Id;
        return new ResultDTO(idParticipant, idTrial, result.Points);
    }
}