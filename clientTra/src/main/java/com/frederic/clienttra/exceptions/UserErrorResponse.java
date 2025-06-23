package com.frederic.clienttra.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserErrorResponse {
    private int state;
    private String message;
    private long timeStamp;

    public UserErrorResponse() {}
    public UserErrorResponse(int state, String message, long timeStamp) {
        this.state = state;
        this.message = message;
        this.timeStamp = timeStamp;
    }

}
