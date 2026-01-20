package com.receiptorc.infrastructure.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestErrorMessage {
    private int status;
    private String message;

    public RestErrorMessage(int status, String message){
        setStatus(status);
        setMessage(message);
    }
}
