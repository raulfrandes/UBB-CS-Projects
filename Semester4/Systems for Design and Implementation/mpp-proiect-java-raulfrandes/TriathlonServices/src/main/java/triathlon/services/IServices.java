package triathlon.services;


import triathlon.model.DTOs.ParticipantDTO;
import triathlon.model.DTOs.TrialDTO;
import triathlon.model.Referee;
import triathlon.model.Result;

import java.util.List;

public interface IServices {
    Referee login(String username, IObserver client) throws ServiceException;
    List<TrialDTO> getTrialNames();
    List<ParticipantDTO> getParticipantsAndPoints() throws ServiceException;
    List<ParticipantDTO> filterParticipantsByTrial(Long idTrial);
    void addResult(Result result) throws ServiceException;
    void logout(Long idReferee, IObserver client) throws ServiceException;
    void sendFilter(Long idTrial);
}
