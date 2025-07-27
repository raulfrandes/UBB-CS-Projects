using System.Collections.Generic;
using Triathlon.Protocol;
using Participant = model.Participant;
using ParticipantDTO = model.DTOs.ParticipantDTO;
using Referee = model.Referee;
using Result = model.Result;
using Trial = model.Trial;
using TrialDTO = model.DTOs.TrialDTO;

namespace networking2;

public class ProtoUtils
{
    public static Response CreateReceivePointsResponse(List<ParticipantDTO> participants)
    {
        Response response = new Response
        {
            Type = Response.Types.Type.ReceivePoints
        };
        foreach (var participant in participants)
        {
            Triathlon.Protocol.ParticipantDTO participantDto = new Triathlon.Protocol.ParticipantDTO
            {
                Id = participant.Id,
                Name = participant.Name,
                Code = participant.Code,
                Points = participant.Points
            };
            response.Participants.Add(participantDto);
        }

        return response;
    }

    public static Response CreateReceivePointsFilteredResponse(List<ParticipantDTO> participants)
    {
        Response response = new Response
        {
            Type = Response.Types.Type.ReceivePointsFiltered
        };
        foreach (var participant in participants)
        {
            Triathlon.Protocol.ParticipantDTO participantDto = new Triathlon.Protocol.ParticipantDTO
            {
                Id = participant.Id,
                Name = participant.Name,
                Code = participant.Code,
                Points = participant.Points
            };
            response.Participants.Add(participantDto);
        }

        return response;
    }

    public static Response CreateLoginResponse(Referee referee)
    {
        Triathlon.Protocol.Referee refereeDto = new Triathlon.Protocol.Referee
        {
            Id = referee.Id,
            Name = referee.Name,
            Username = referee.Username,
            Password = referee.Password
        };
        return new Response
        {
            Type = Response.Types.Type.Login,
            Referee = refereeDto,
        };
    }

    public static Response CreateErrorResponse(string error)
    {
        return new Response
        {
            Type = Response.Types.Type.Error,
            Message = error
        };
    }

    public static Response CreateOkResponse()
    {
        return new Response
        {
            Type = Response.Types.Type.Ok
        };
    }

    public static Response CreateGetParticipantsResponse(List<ParticipantDTO> participants)
    {
        Response response = new Response
        {
            Type = Response.Types.Type.GetParticipants
        };
        foreach (var participant in participants)
        {
            Triathlon.Protocol.ParticipantDTO participantDto = new Triathlon.Protocol.ParticipantDTO
            {
                Id = participant.Id,
                Name = participant.Name,
                Code = participant.Code,
                Points = participant.Points
            };
            response.Participants.Add(participantDto);
        }

        return response;
    }

    public static Response CreateGetTrialsResponse(List<TrialDTO> trials)
    {
        Response response = new Response
        {
            Type = Response.Types.Type.GetTrials
        };
        foreach (var trial in trials)
        {
            Triathlon.Protocol.TrialDTO trialDto = new Triathlon.Protocol.TrialDTO
            {
                Id = trial.Id,
                Name = trial.Name
            };
            response.Trials.Add(trialDto);
        }

        return response;
    }
    
    public static Result GetResult(Request request)
    {
        Trial trial = new Trial(request.Result.Trial.Name, null, null);
        trial.Id = request.Result.Trial.Id;
        
        Participant participant = new Participant(request.Result.Participant.Name, request.Result.Participant.Code);
        participant.Id = request.Result.Participant.Id;
        
        Result result = new Result(participant, trial, request.Result.Points);
        
        return result;
    }
}