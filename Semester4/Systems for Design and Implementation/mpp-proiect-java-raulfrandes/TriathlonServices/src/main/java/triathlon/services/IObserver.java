package triathlon.services;

import triathlon.model.DTOs.ParticipantDTO;

import java.util.List;

public interface IObserver {
    void pointsReceived(List<ParticipantDTO> participants) throws ServiceException;
    void pointsReceivedFiltered(List<ParticipantDTO> participants) throws ServiceException;
    Long getFilter();
}
