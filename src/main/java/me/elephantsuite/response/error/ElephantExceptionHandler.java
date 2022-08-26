package me.elephantsuite.response.error;

import me.elephantsuite.response.Response;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ElephantExceptionHandler {


    @ExceptionHandler(value = NullPointerException.class)
    public Response handleNpe(NullPointerException e) {
        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.FAILURE, "NPE while processing request!")
                .addException(e)
                .build();
    }
}