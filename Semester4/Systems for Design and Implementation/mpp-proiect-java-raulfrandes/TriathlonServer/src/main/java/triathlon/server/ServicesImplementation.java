package triathlon.server;

import triathlon.model.DTOs.ParticipantDTO;
import triathlon.model.DTOs.TrialDTO;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.model.Trial;
import triathlon.persistence.interfaces.IParticipantRepository;
import triathlon.persistence.interfaces.IRefereeRepository;
import triathlon.persistence.interfaces.IResultRepository;
import triathlon.persistence.interfaces.ITrialRepository;
import triathlon.services.IObserver;
import triathlon.services.IServices;
import triathlon.services.ServiceException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServicesImplementation implements IServices {
    private final IRefereeRepository refereeRepository;
    private final ITrialRepository trialRepository;
    private final IParticipantRepository participantRepository;
    private final IResultRepository resultRepository;
    private Map<Long, IObserver> clients;

    public ServicesImplementation(IRefereeRepository refereeRepository, ITrialRepository trialRepository,
                   IParticipantRepository participantRepository, IResultRepository resultRepository) {
        this.refereeRepository = refereeRepository;
        this.trialRepository = trialRepository;
        this.participantRepository = participantRepository;
        this.resultRepository = resultRepository;
        clients = new ConcurrentHashMap<>();
    }

    public synchronized Referee login(String username, IObserver client) throws ServiceException{
        Referee referee = refereeRepository.findByUsername(username);
        if (referee == null) {
            throw new ServiceException("Wrong credentials!");
        }
        clients.put(referee.getId(), client);
        return referee;
    }

    private Trial findTrialById(Long idTrial) throws ServiceException {
        Trial trial = trialRepository.findById(idTrial);
        if (trial == null) {
            throw new ServiceException("Trial not found");
        }
        return  trial;
    }

    public synchronized List<TrialDTO> getTrialNames() {
        Iterable<Trial> trials = trialRepository.findAll();
        return StreamSupport.stream(trials.spliterator(), false)
                .map(trial -> new TrialDTO(trial.getId(), trial.getName()))
                .collect(Collectors.toList());
    }

    private Participant findParticipantById(Long idParticipant) throws ServiceException {
        Participant participant = participantRepository.findById(idParticipant);
        if (participant == null) {
            throw new ServiceException("Participant not found");
        }
        return participant;
    }

    public synchronized List<ParticipantDTO> getParticipantsAndPoints() {
        Iterable<Participant> participants = participantRepository.findAll();
        return StreamSupport.stream(participants.spliterator(), false)
                .sorted(Comparator.comparing(Participant::getName))
                .map(participant -> {
                    List<Result> results = resultRepository.findByParticipant(participant.getId());
                    Integer points = results.stream().mapToInt(Result::getPoints).sum();
                    return new ParticipantDTO(
                            participant.getId(),
                            participant.getName(),
                            participant.getCode(),
                            points
                    );
                }).collect(Collectors.toList());
    }

    public synchronized List<ParticipantDTO> filterParticipantsByTrial(Long idTrial) {
        List<Result> results = resultRepository.findByTrial(idTrial);
        return results.stream()
                .map(result -> {
                    Participant participant = result.getParticipant();
                    return new ParticipantDTO(
                            participant.getId(),
                            participant.getName(),
                            participant.getCode(),
                            result.getPoints()
                    );
                })
                .sorted(Comparator.comparing(ParticipantDTO::getPoints).reversed())
                .collect(Collectors.toList());
    }

    public synchronized void addResult(Result result) throws ServiceException {
        Long idParticipant = result.getParticipant().getId();
        Participant participant = findParticipantById(idParticipant);
        Long idTrial = result.getTrial().getId();
        Trial trial = findTrialById(idTrial);
        Result newResult = new Result(participant, trial, result.getPoints());
        Result addedResult = resultRepository.save(newResult);
        if (addedResult == null) {
            throw new ServiceException("Add points failed!");
        }
        clients.forEach((key, value) -> {
            value.pointsReceived(getParticipantsAndPoints());
            Long filter = value.getFilter();
            if (filter != null) {
                value.pointsReceivedFiltered(filterParticipantsByTrial(filter));
            }
        });
    }

    @Override
    public synchronized void logout(Long id, IObserver client) throws ServiceException {
        IObserver localClient = clients.remove(id);
        if (localClient == null) {
            throw new ServiceException("Referee " + id + " is not logged in!");
        }
    }

    @Override
    public void sendFilter(Long idTrial) {}
}
