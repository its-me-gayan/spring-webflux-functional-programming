package org.ryanair.flight.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BackendInvocationException extends RuntimeException{
    private HttpStatus status;
    private String messageDescription;
    public BackendInvocationException(String message,String messageDescription , HttpStatus status) {
        super(message);
        this.status = status;
        this.messageDescription = messageDescription;
    }
}
