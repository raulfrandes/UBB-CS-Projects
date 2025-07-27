package triathlon.networking.protocol.requests;

import triathlon.networking.protocol.Request;

public class SendFilterRequest implements Request {
    private Long idTrial;

    public SendFilterRequest(Long idTrial) {
        this.idTrial = idTrial;
    }

    public Long getIdTrial() {
        return idTrial;
    }
}
