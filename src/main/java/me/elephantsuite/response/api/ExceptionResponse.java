package me.elephantsuite.response.api;

import lombok.Getter;
import me.elephantsuite.response.util.ResponseStatus;

@Getter
public class ExceptionResponse extends Response {

    private final String exception;

    ExceptionResponse(ResponseStatus status, String message, String exception) {
        super(status, message);
        this.exception = exception;
    }
}
