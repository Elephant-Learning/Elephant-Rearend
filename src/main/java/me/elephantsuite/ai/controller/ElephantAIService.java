package me.elephantsuite.ai.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import me.elephantsuite.ai.ChatGPTService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.util.ResponseStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantAIService {

	private final String deckPromptString = "write an unordered list (without a dash preceding) of [NUMBER] terms and extremely concise definitions regarding [TOPIC] where each term can have multiple incredibly concise definitions if necessary. Write only the terms and definitions in the form outlined below:\n\nterm -  definition, definition #2 (optional), ...";

	public Response sendMessage(ElephantAIRequest.SendMessage request) {
		String prompt = request.getPrompt();

		String message = getResponse(prompt);

		return ResponseBuilder.create()
			.addResponse(ResponseStatus.SUCCESS, "Created Chat!")
			.addObject("message", message)
			.build();
	}

	public Response createDeck(ElephantAIRequest.CreateDeck request) {
		long userId = request.getUserId();
		String topic = request.getTopic();
		int numberOfTerms = request.getTermNumber();

		String query = deckPromptString.replace("[NUMBER]", String.valueOf(numberOfTerms)).replace("[TOPIC]", topic);

		String response = getResponse(query);

		List<String> terms = Arrays.asList(response.split("\n"));

		return null;
	}

	private static String getResponse(String prompt) {
		JsonObject object = null;
		try {
			object = ChatGPTService.sendMessage(prompt).getAsJsonObject();
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

		return object.get("choices").getAsJsonArray().get(0).getAsJsonObject().get("message").getAsJsonObject().get("content").getAsString();
	}
}
