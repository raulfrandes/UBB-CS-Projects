using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Remoting;
using model;
using model.DTOs;
using persistence.interfaces;
using services;

namespace server;

public class ServerImplementation : IServices
{
    private IRefereeRepository _refereeRepository;
    private ITrialRepository _trialRepository;
    private IParticipantRepository _participantRepository;
    private IResultRepository _resultRepository;
    private readonly IDictionary<long, IObserver> clients;
        
    public ServerImplementation(IRefereeRepository refereeRepository, ITrialRepository trialRepository,
        IParticipantRepository participantRepository, IResultRepository resultRepository)
    {
        _refereeRepository = refereeRepository;
        _trialRepository = trialRepository;
        _participantRepository = participantRepository;
        _resultRepository = resultRepository;
        clients = new Dictionary<long, IObserver>();
    }

    public Referee Login(string username, IObserver client)
    {
        Referee? referee = _refereeRepository.FindByUsername(username);
        if (referee == null)
        {
            throw new ServiceException("Referee not found");
        }

        if (clients.ContainsKey(referee.Id))
        {
            throw new ServiceException("User already logged in");
        }
        clients[referee.Id] = client;
        return referee;
    }

    private Trial FindTrialById(long idTrial)
    {
        Trial? trial = _trialRepository.FindById(idTrial);
        if (trial == null)
        {
            throw new ServiceException("Trial not found");
        }

        return trial;
    }

    public List<TrialDTO> GetTrialNames()
    {
        return _trialRepository.FindAll()
            .Select(trial => new TrialDTO(trial.Id, trial.Name))
            .ToList();
    }

    private Participant FindParticipantById(long idParticipant)
    {
        Participant? participant = _participantRepository.FindById(idParticipant);
        if (participant == null)
        {
            throw new ServiceException("Participant not found");
        }

        return participant;
    }

    public List<ParticipantDTO> GetParticipantsAndPoints()
    {
        return _participantRepository.FindAll()
            .OrderBy(participant => participant.Name)
            .Select(participant =>
            {
                List<Result> results = _resultRepository.FindByParticipant(participant.Id).ToList();
                int points = results.Sum(result => result.Points);
                return new ParticipantDTO(
                    participant.Id,
                    participant.Name,
                    participant.Code,
                    points
                );
            }).ToList();
    }

    public List<ParticipantDTO> FilterParticipantsByTrial(long idTrial)
    {
        return _resultRepository.FindByTrial(idTrial)
            .Select(result =>
            { 
                Participant participant = result.Participant;
                return new ParticipantDTO(
                    participant.Id,
                    participant.Name,
                    participant.Code,
                    result.Points
                );
            })
            .OrderByDescending(participant => participant.Points)
            .ToList();
    }

    public void AddResult(Result result)
    {
        long idParticipant = result.Participant.Id;
        Participant participant = FindParticipantById(idParticipant);
        long idTrial = result.Trial.Id;
        Trial trial = FindTrialById(idTrial);
        Result newResult = new Result(participant, trial, result.Points);
        Result? addedResult = _resultRepository.Save(newResult);
        if (addedResult == null)
        {
            throw new ServiceException("Add points failed!");
        }

        foreach (var client in clients)
        {
            client.Value.PointsReceived(GetParticipantsAndPoints());
            long? filter = client.Value.GetFilter();
            if (filter != null)
            {
                client.Value.PointsReceivedFiltered(FilterParticipantsByTrial(filter.Value));
            }
        }
    }
    
    public void Logout(long idReferee, IObserver client)
    {
        IObserver localClient = clients[idReferee];
        if (localClient == null)
        {
            throw new ServerException("Referee " + idReferee + " is not logged in.");
        }

        clients.Remove(idReferee);
    }

    public void SendFilter(long idTrial)
    {
    }
}