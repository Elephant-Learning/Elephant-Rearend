package me.elephantsuite.response.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.google.common.base.Throwables;
import me.elephantsuite.response.util.ResponseStatus;

// Builds a response that is then sent to elephant client
public class ResponseBuilder {


	private ResponseStatus status;

	private String message;

	private Map<String, Object> objMap = new HashMap<>();

	private Throwable throwable = null;

	// more of a preference, but imo builders should be initialized static, not through constructor
	private ResponseBuilder() {}

	public static ResponseBuilder create() {
		return new ResponseBuilder();
	}

	public ResponseBuilder addResponse(ResponseStatus status, String message) {
		this.status = status;
		this.message = message;
		return this;
	}


	public ResponseBuilder addObject(String key, Object obj) {
		this.objMap.put(key, obj);
		return this;
	}

	public ResponseBuilder addException(Throwable t) {
		this.throwable = Throwables.getRootCause(t);

		return this;
	}

	public Response build() {
		Objects.requireNonNull(message, "Must set a message for the Response");
		Objects.requireNonNull(status, "Must set a status to create a Response");

		if (this.throwable != null) {
			return new ExceptionResponse(status, message, throwable.getMessage());
		}

		if (this.objMap.isEmpty()) {
			return new Response(status, message);
		}

		return new ContextResponse(status, message, objMap);
	}


}
