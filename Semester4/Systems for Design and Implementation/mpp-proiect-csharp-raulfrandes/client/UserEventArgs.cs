using System;

namespace client;

public enum UserEvent
{
    PointsReceived, PointsReceivedFiltered, GetFilter
}

public class UserEventArgs : EventArgs
{
    private readonly UserEvent _userEvent;
    private readonly Object _data;

    public UserEventArgs(UserEvent userEvent, object data)
    {
        _userEvent = userEvent;
        _data = data;
    }
    
    public UserEvent UserEvent => _userEvent;

    public object Data => _data;
}