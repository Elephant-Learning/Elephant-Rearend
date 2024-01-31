package me.elephantsuite.response.exception;

import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;

// throw this exception if we get an error from an external API (like ChatGPT)
public class APIException extends RuntimeException {

	private final Map<String, JsonElement> error;

	public APIException(JsonObject error) {
		this.error = error.asMap();
	}

	public Response toResponse() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.FAILURE, "API Exception")
			.addObject("error", error)
			.build();
	}
}
