package me.elephantsuite.response;

import lombok.Getter;

@Getter
public class ExceptionResponse extends Response {

    private final String exception;

    ExceptionResponse(ResponseStatus status, String message, String exception) {
        super(status, message);
        this.exception = exception;
    }
}
