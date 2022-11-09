package me.elephantsuite.response.exception;

import java.util.Arrays;
import java.util.stream.Collectors;

import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;

public class InvalidIdException extends RuntimeException {

	private final Object request;

	private final InvalidIdType[] types;

	public InvalidIdException(Object request, InvalidIdType... types) {
		this.request = request;
		this.types = types;
	}

	public Response toResponse() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "Invalid " + typesToString(types) + " ID!")
			.addObject(this.request.getClass().getName().toLowerCase().contains("long") ? "id" : "request", this.request)
			.build();
	}

	private static String typesToString(InvalidIdType[] types) {
		return Arrays
			.stream(types)
			.map(InvalidIdType::getName)
			.collect(Collectors.joining(", "));
	}

}
