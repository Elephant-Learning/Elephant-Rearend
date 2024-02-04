package me.elephantsuite.ai.controller;

import static me.elephantsuite.deck.controller.DeckService.convertToCards;
import static me.elephantsuite.deck.controller.DeckService.hasInvalidTag;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.ai.ChatGPTService;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.DeckVisibility;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.deck.controller.DeckRequest;
import me.elephantsuite.deck.controller.DeckService;
import me.elephantsuite.registration.RegistrationService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.stats.medal.MedalService;
import me.elephantsuite.stats.medal.MedalType;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ElephantAIService {

	private final String deckPromptString = "write an unordered list (without a dash preceding) of [NUMBER] terms and extremely concise definitions regarding [TOPIC] where each term can have multiple incredibly concise definitions if necessary. Write only the terms and definitions in the form outlined below:\n\nterm -  definition, definition #2 (optional), ...\n Please prefix each term with a \"-\".";

	private DeckRepositoryService service;

	private ElephantUserService userService;

	private CardService cardService;

	private MedalService medalService;

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

		ElephantBackendApplication.LOGGER.info(response);

		List<String> terms = Arrays.asList(response.split("\n"));

		terms = terms.subList(1, terms.size());

		ElephantBackendApplication.LOGGER.info("Terms: " + terms);

		terms = terms
			.stream()
			.map(String::trim)
			.map(s -> s.substring(s.indexOf(" ") + 1))
			.toList();

		ElephantBackendApplication.LOGGER.info("Terms After: " + terms);

		Map<String, List<String>> keyToDefinitions = new HashMap<>();

		terms.forEach(s -> {
			ElephantBackendApplication.LOGGER.info(s);
			String term = s.substring(0, s.indexOf("-")).trim();
			String def = s.substring(s.indexOf("-") + 1).trim();
			keyToDefinitions.put(term, List.of(def));
		});

		DeckRequest.CreateDeck createDeck = new DeckRequest.CreateDeck(keyToDefinitions, userId, topic, request.getDeckVisibility());

		return registerDeck(createDeck);
	}

	public Response registerDeck(DeckRequest.CreateDeck request) {
		Map<String, List<String>> terms = request.getTerms();
		long authorId = request.getAuthorId();
		String name = request.getName();
		DeckVisibility visibility = request.getVisibility();

		ElephantUser user = ResponseUtil.checkUserValid(authorId, userService);


		if (RegistrationService.isInvalidName(name)) {
			throw new InvalidTagInputException(name);
		}

		String error = hasInvalidTag(terms);

		if (error != null) {
			throw new InvalidTagInputException(error);
		}

		Deck deck = new Deck(null, user, name, visibility);

		List<Card> cards = convertToCards(terms, deck ,cardService);

		deck.setCards(cards);

		user.getDecks().add(deck);

		deck = service.saveDeck(deck);

		// user = userService.saveUser(user);

		// medalService.updateEntityMedals(user.getDecks(), user.getElephantUserStatistics(), MedalType.FLIP_MASTER, new int[]{2, 8, 16, 64});

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Created Deck!")
			.addObject("user", user)
			.addObject("deck", deck)
			.build();
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
