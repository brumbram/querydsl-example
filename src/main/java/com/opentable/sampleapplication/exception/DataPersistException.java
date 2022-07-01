package com.opentable.sampleapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Error while persisting data")
public class DataPersistException extends RuntimeException {

    public DataPersistException(String message, Throwable cause) {
        super(message, cause);
    }
}
