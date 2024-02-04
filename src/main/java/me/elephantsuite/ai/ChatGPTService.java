package me.elephantsuite.ai;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.response.exception.APIException;
import org.springframework.stereotype.Service;

public class ChatGPTService {

	private static final String API_URL = "https://api.openai.com/v1/chat/completions";

	private static final String API_KEY = ElephantBackendApplication.AI_INTEGRATION.getConfigOption("chatGptApiKey");

	private static final Gson GSON = new GsonBuilder()
		.serializeNulls()
		.setPrettyPrinting()
		.create();

	public static JsonElement sendMessage(String prompt) throws IOException, InterruptedException {
		JsonObject object = new JsonObject();
		object.addProperty("model", "gpt-3.5-turbo");
		JsonArray messages = new JsonArray();
		JsonObject promptObj = new JsonObject();
		promptObj.addProperty("role", "user");
		promptObj.addProperty("content", prompt);
		messages.add(promptObj);
		object.add("messages", messages);

		String body = GSON.toJson(object);

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(API_URL))
			.header("Content-Type", "application/json")
			.header("Authorization", "Bearer " + API_KEY)
			.POST(HttpRequest.BodyPublishers.ofString(body))
			.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		if (response.statusCode() == 200) {
			return JsonParser.parseString(response.body());
		} else {
			ElephantBackendApplication.LOGGER.error(JsonParser.parseString(response.body()));
			throw new APIException(JsonParser.parseReader(new StringReader(response.body())).getAsJsonObject());
		}
	}

	public static void main(String[] args) {
		try {
			JsonElement response = sendMessage("Hi!");
			System.out.println(GSON.toJson(response));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



}
