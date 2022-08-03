package me.elephantsuite.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.google.common.base.Throwables;

// Builds a response that is then sent to elephant client
public class ResponseBuilder {


	private ResponseStatus status;

	private String message;

	private Map<String, Object> objMap = new HashMap<>();

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
		this.objMap.put("exception", t);
		this.objMap.put("root", Throwables.getRootCause(t));
		this.objMap.put("stacktrace", List.of(t.getStackTrace()));

		return this;
	}

	public Response build() {
		Objects.requireNonNull(message, "Must set a message for the Response");
		Objects.requireNonNull(status, "Must set a status to create a Response");

		if (this.objMap.isEmpty()) {
			return new Response(status, message);
		}

		return new ContextResponse(status, message, objMap);
	}


}
