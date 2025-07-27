package triathlon.networking.protocol.requests;

import triathlon.networking.protocol.Request;

public class LogoutRequest implements Request {
    private Long id;

    public LogoutRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
