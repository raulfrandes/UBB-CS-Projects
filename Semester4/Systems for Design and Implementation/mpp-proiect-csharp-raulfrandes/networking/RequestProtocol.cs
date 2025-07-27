using System;
using networking.dtos;

namespace networking;

public interface IRequest{}

[Serializable]
public class LoginRequest : IRequest
{
    private string _username;

    public LoginRequest(string username)
    {
        _username = username;
    }

    public virtual string Username => _username;
}

[Serializable]
public class LogoutRequest(long id) : IRequest
{
    private long _id = id;

    public virtual long Id => _id;
}

[Serializable]
public class AddResultRequest(ResultDTO result) : IRequest
{
    private ResultDTO _result = result;

    public virtual ResultDTO Result => _result;
}

[Serializable]
public class GetFilteredParticipantsRequest(long idTrial) : IRequest
{
    private long _idTrial = idTrial;

    public virtual long IdTrial => _idTrial;
}

[Serializable]
public class GetParticipantsRequest : IRequest
{
}

[Serializable]
public class GetTrialsRequest : IRequest
{
}

[Serializable]
public class SendFilterRequest(long idTrial) : IRequest
{
    private long _idTrial = idTrial;

    public virtual long IdTrial => _idTrial;
}