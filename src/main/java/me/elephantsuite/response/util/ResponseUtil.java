package me.elephantsuite.response.util;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.UserNotEnabledException;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;

public class ResponseUtil {

	public static ElephantUser checkUserValid(long userId, ElephantUserService service) {
		ElephantUser user = service.getUserById(userId);

		if (user == null) {
			throw new InvalidIdException(userId, InvalidIdType.USER);
		}

		if (!user.isEnabled()) {
			throw new UserNotEnabledException(user);
		}

		return user;
	}

	public static Response getFailureResponse(String message, Object request) {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, message)
			.addObject("request", request)
			.build();
	}
}
