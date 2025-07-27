package triathlon.networking.protocol.responses;

import triathlon.model.DTOs.TrialDTO;
import triathlon.networking.protocol.Response;

public class GetTrialsResponse implements Response {
    private TrialDTO[] trials;

    public GetTrialsResponse(TrialDTO[] trials) {
        this.trials = trials;
    }

    public TrialDTO[] getTrials() {
        return trials;
    }
}
