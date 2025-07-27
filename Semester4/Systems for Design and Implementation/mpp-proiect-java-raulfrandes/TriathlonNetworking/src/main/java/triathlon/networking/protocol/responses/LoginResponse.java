package triathlon.networking.protocol.responses;

import triathlon.model.Referee;
import triathlon.networking.protocol.Response;

public class LoginResponse implements Response {
    private Referee referee;

    public LoginResponse(Referee referee) {
        this.referee = referee;
    }

    public Referee getReferee() {
        return referee;
    }
}
