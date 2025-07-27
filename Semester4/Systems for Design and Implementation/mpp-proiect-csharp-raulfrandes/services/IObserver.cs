using System.Collections.Generic;
using model.DTOs;

namespace services;

public interface IObserver
{
    void PointsReceived(List<ParticipantDTO> participants);
    void PointsReceivedFiltered(List<ParticipantDTO> participants);
    long GetFilter();
}