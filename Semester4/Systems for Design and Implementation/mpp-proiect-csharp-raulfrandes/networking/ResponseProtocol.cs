using System;
using model;
using model.DTOs;

namespace networking;

public interface IResponse
{
}

[Serializable]
public class OkResponse : IResponse
{
}

[Serializable]
public class ErrorResponse(string message) : IResponse
{
    private string _message = message;

    public virtual string Message => _message;
}

[Serializable]
public class GetParticipantsResponse(ParticipantDTO[] participants) : IResponse
{
    private ParticipantDTO[] _participants = participants;

    public virtual ParticipantDTO[] Participants => _participants;
}

[Serializable]
public class GetTrialsResponse(TrialDTO[] trials) : IResponse
{
    private TrialDTO[] _trials = trials;

    public virtual TrialDTO[] Trials => _trials;
}

[Serializable]
public class LoginResponse : IResponse
{
    private Referee _referee;

    public LoginResponse(Referee referee)
    {
        _referee = referee;
    }

    public virtual Referee Referee => _referee;
}

[Serializable]
public class LogoutResponse : IResponse
{
}

public interface IUpdateResponse : IResponse
{
}

[Serializable]
public class ReceivePointsFilteredResponse(ParticipantDTO[] participants) : IUpdateResponse
{
    private ParticipantDTO[] _participants = participants;

    public virtual ParticipantDTO[] Participants => _participants;
}

[Serializable]
public class ReceivePointsResponse(ParticipantDTO[] participants) : IUpdateResponse
{
    private ParticipantDTO[] _participants = participants;
    
    public virtual ParticipantDTO[] Participants => _participants;
}