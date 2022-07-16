package me.elephantsuite.response;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import me.elephantsuite.registration.token.ConfirmationToken;
import org.hibernate.proxy.HibernateProxy;

// Builds a response that is then sent to elephant client
public class ResponseBuilder {

	private static final Gson GSON = new GsonBuilder()
		.setPrettyPrinting()
		.serializeNulls()
		.create();

	private final JsonObject object = new JsonObject();

	// more of a preference, but imo builders should be initialized static, not through constructor
	private ResponseBuilder() {}

	public static ResponseBuilder create() {
		return new ResponseBuilder();
	}


	public ResponseBuilder addResponse(ResponseStatus status, String message) {
		this.object.addProperty("status", status.toString());
		this.object.addProperty("message", message);
		return this;
	}

	// am lazy
	public ResponseBuilder addValue(Consumer<JsonObject> objectConsumer) {
		objectConsumer.accept(this.object);
		return this;
	}

	public ResponseBuilder addToken(ConfirmationToken src) {

		JsonObject object = new JsonObject();

		object.addProperty("id", src.getId());
		object.addProperty("token", src.getToken());
		object.addProperty("createdAt", src.getCreatedAt().toString());
		object.addProperty("expiresAt", src.getExpiresAt().toString());
		object.add("user", GSON.toJsonTree(src.getElephantUser()));

		this.object.add("token", object);

		return this;
	}

	public ResponseBuilder addObject(String key, Object obj) {
		JsonElement element = GSON.toJsonTree(obj);
		this.object.add(key, element);
		return this;
	}

	public String build() {
		return GSON.toJson(this.object);
	}
}
