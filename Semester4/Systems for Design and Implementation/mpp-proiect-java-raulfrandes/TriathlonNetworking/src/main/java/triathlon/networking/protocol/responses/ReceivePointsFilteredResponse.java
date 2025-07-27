package triathlon.networking.protocol.responses;

import triathlon.model.DTOs.ParticipantDTO;
import triathlon.networking.protocol.Response;
import triathlon.networking.protocol.UpdateResponse;

public class ReceivePointsFilteredResponse implements UpdateResponse {
    private ParticipantDTO[] participants;

    public ReceivePointsFilteredResponse(ParticipantDTO[] participants) {
        this.participants = participants;
    }

    public ParticipantDTO[] getParticipants() {
        return participants;
    }
}
