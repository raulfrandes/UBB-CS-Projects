package triathlon.networking.protocol.requests;

import triathlon.networking.protocol.Request;

public class GetFilteredParticipantsRequest implements Request {
    private Long idTrial;

    public GetFilteredParticipantsRequest(Long idTrial) {
        this.idTrial = idTrial;
    }

    public Long getIdTrial() {
        return idTrial;
    }
}
