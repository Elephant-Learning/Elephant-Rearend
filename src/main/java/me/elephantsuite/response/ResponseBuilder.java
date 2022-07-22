package me.elephantsuite.response;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.google.common.base.Throwables;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.elephantsuite.registration.token.ConfirmationToken;
import org.hibernate.proxy.HibernateProxy;

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
		return new Response(Objects.requireNonNull(status), Objects.requireNonNull(message), objMap);
	}


}
