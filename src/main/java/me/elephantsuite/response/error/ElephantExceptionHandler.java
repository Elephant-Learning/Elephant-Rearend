package me.elephantsuite.response.error;

import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.APIException;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidPasswordException;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.exception.InvalidUserAuthorizationException;
import me.elephantsuite.response.exception.UserNotEnabledException;
import me.elephantsuite.response.util.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ElephantExceptionHandler {


    @ExceptionHandler(value = NullPointerException.class)
    public Response handleNpe(NullPointerException e) {
        ElephantBackendApplication.LOGGER.error("NPE while processing request!", e);
        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.FAILURE, "NPE while processing request!")
                .addException(e)
                .build();
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public Response handleIllegalArgumentException(IllegalArgumentException e) {
        ElephantBackendApplication.LOGGER.error("Illegal Argument Exception while processing request!", e);
        return ResponseBuilder
            .create()
            .addResponse(ResponseStatus.FAILURE, "Illegal Argument Exception while processing request!")
            .addException(e)
            .build();
    }

    @ExceptionHandler(value = InvalidIdException.class)
    public Response handleInvalidIdException(InvalidIdException e) {
        return e.toResponse();
    }

    @ExceptionHandler(value = UserNotEnabledException.class)
    public Response handleUserNotEnabledException(UserNotEnabledException e) {
        return e.toResponse();
    }

    @ExceptionHandler(value = InvalidPasswordException.class)
    public Response handleInvalidPasswordException(InvalidPasswordException e) {
        return e.toResponse();
    }

    @ExceptionHandler(value = InvalidUserAuthorizationException.class)
    public Response handleInvalidUserAuthorizationException(InvalidUserAuthorizationException e) {
        return e.toResponse();
    }

    @ExceptionHandler(value = InvalidTagInputException.class)
    public Response handleInvalidTagInputException(InvalidTagInputException e) {
        return e.toResponse();
    }

    @ExceptionHandler(value = APIException.class)
    public Response handleAPIException(APIException e) {
        return e.toResponse();
    }
}
