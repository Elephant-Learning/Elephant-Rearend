package me.elephantsuite.response.util;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;

public class ResponseUtil {

	public static Response getInvalidUserResponse(long userId) {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
			.addObject("userId", userId)
			.build();
	}

	public static Response getInvalidUserResponse(Object request) {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
			.addObject("request", request)
			.build();
	}

	public static Response getFailureResponse(String message, Object request) {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, message)
			.addObject("request", request)
			.build();
	}
}
