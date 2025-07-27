package triathlon.networking.protocol.responses;

import triathlon.model.DTOs.ParticipantDTO;
import triathlon.networking.protocol.Response;

public class GetParticipantsResponse implements Response {
    private ParticipantDTO[] participants;

    public GetParticipantsResponse(ParticipantDTO[] participants) {
        this.participants = participants;
    }

    public ParticipantDTO[] getParticipants() {
        return participants;
    }
}
