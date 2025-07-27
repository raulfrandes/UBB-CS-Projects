using System;
using System.Collections.Generic;
using System.Windows.Forms;
using model;
using model.DTOs;
using services;

namespace client;

public class TriathlonController : IObserver
{
    public event EventHandler<UserEventArgs> updateEvent;
    private readonly IServices _server;
    private Referee _referee;
    private long _filter;

    public TriathlonController(IServices server)
    {
        _server = server;
        _referee = null!;
    }

    public void HandleLogin(String username, String password)
    {
        _referee = _server.Login(username, this);
        if (_referee.Password.Equals(password))
        {
            Form refereeForm = new RefereeProfile(this, _referee);
            refereeForm.Show();
        }
        else
        {
            throw new ServiceException("Wrong credentials!");
        }
    }

    public List<ParticipantDTO> LoadParticipants()
    {
        return _server.GetParticipantsAndPoints();
    }
    
    public List<ParticipantDTO> LoadParticipantsFiltered(long idTrial)
    {
        return _server.FilterParticipantsByTrial(idTrial);
    }

    public List<TrialDTO> LoadTrials()
    {
        return _server.GetTrialNames();
    }

    public void HandleAddPoints(long idParticipant, long idTrial, int points)
    {
        Participant participant = new Participant("", "");
        participant.Id = idParticipant;
        Trial trial = new Trial("", "", null!);
        trial.Id = idTrial;
        Result result = new Result(participant, trial, points);
        _server.AddResult(result);
    }

    public void HandleLogout()
    {
        Console.WriteLine(@"Logout");
        _server.Logout(_referee.Id, this);
        _referee = null!;
    }

    public void HandleSendFilter(long idTrial)
    {
        _filter = idTrial;
        _server.SendFilter(_filter);
    }
    
    public void PointsReceived(List<ParticipantDTO> participants)
    {
        UserEventArgs userEventArgs = new UserEventArgs(UserEvent.PointsReceived, participants);
        Console.WriteLine(@"Points received");
        OnUserEvent(userEventArgs);
    }

    public void PointsReceivedFiltered(List<ParticipantDTO> participants)
    {
        UserEventArgs userEventArgs = new UserEventArgs(UserEvent.PointsReceivedFiltered, participants);
        Console.WriteLine(@"Points received filtered");
        OnUserEvent(userEventArgs);
    }

    public long GetFilter()
    {
        return _filter;
    }

    protected virtual void OnUserEvent(UserEventArgs e)
    {
        if (updateEvent == null) return;
        updateEvent(this, e);
        Console.WriteLine(@"Update Event called");
    }
}