package triathlon.networking.protocol.requests;

import triathlon.networking.protocol.Request;

public class LoginRequest implements Request {
    private String username;

    public LoginRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
