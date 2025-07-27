package triathlon.networking.protocol.requests;

import triathlon.networking.protocol.Request;
import triathlon.networking.protocol.dto.ResultDTO;

public class AddResultRequest implements Request {
    private ResultDTO result;

    public AddResultRequest(ResultDTO result) {
        this.result = result;
    }

    public ResultDTO getResult() {
        return result;
    }
}
