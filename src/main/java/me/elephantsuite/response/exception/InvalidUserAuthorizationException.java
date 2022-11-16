package me.elephantsuite.response.exception;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.user.ElephantUserType;

public class InvalidUserAuthorizationException extends RuntimeException {

    private final ElephantUserType requiredType;

    public InvalidUserAuthorizationException(ElephantUserType requiredType) {
        this.requiredType = requiredType;
    }

    public Response toResponse() {
        return ResponseBuilder
                .create()
                .addResponse(ResponseStatus.FAILURE, String.format("You do not have the %s user type required to run this command!", requiredType.toString()))
                .addObject("requiredType", requiredType)
                .build();
    }
}
