using System.Collections.Generic;
using model;
using model.DTOs;

namespace services;

public interface IServices
{
    Referee Login(string username, IObserver client);
    List<TrialDTO> GetTrialNames();
    List<ParticipantDTO> GetParticipantsAndPoints();
    List<ParticipantDTO> FilterParticipantsByTrial(long idTrial);
    void AddResult(Result result);
    void Logout(long idReferee, IObserver client);
    void SendFilter(long idTrial);
}