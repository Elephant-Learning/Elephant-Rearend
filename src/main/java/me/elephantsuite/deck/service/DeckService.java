package me.elephantsuite.deck.service;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.response.ResponseBuilder;
import me.elephantsuite.response.ResponseStatus;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class DeckService {

	private final ElephantUserService userService;

	private final DeckRepositoryService service;

	public String createDeck(DeckRequest.CreateDeck request) {
		Map<String, List<String>> terms = request.getTerms();
		long authorId = request.getAuthorId();
		String name = request.getName();

		ElephantUser user = userService.getUserById(authorId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Author ID Given!")
				.addObject("request", request)
				.build();
		}

		Deck deck = new Deck(terms, user, name);

		user.getDecks().add(deck);

		deck = service.saveDeck(deck);

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Created Deck!")
			.addObject("user", user)
			.addObject("deck", deck)
			.build();
	}

	public String likeDeck(DeckRequest.LikeDeck likeDeck) {
		Deck deck = service.getDeckById(likeDeck.getDeckId());

		ElephantUser user = userService.getUserById(likeDeck.getUserId());

		if (deck == null || user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck Or User ID!")
				.addValue(jsonObject -> jsonObject.addProperty("deckId", likeDeck.getDeckId()))
				.addValue(jsonObject -> jsonObject.addProperty("userId", likeDeck.getUserId()))
				.build();
		}

		deck.likeDeck();

		deck = service.saveDeck(deck);

		user.getLikedDecksIds().add(likeDeck.getDeckId());

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Favorited Deck!")
			.addObject("deck", deck)
			.addObject("user", user)
			.build();
	}

	public List<Deck> getAllDecks() {
		return service.getAllDecks();
/*
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved All Decks!")
			.addList("decks", decks)
			.build();

 */
	}

	public String renameDeck(DeckRequest.RenameDeck renameDeck) {
		Deck deck = service.getDeckById(renameDeck.getDeckId());

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addValue(jsonObject -> jsonObject.addProperty("deckId", renameDeck.getDeckId()))
				.build();
		}

		deck.setName(renameDeck.getNewName());

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Renamed Deck!")
			.addObject("deck", deck)
			.build();
	}


	public String addTerms(DeckRequest.AddTerms addTerms) {
		Deck deck = service.getDeckById(addTerms.getDeckId());

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addValue(jsonObject -> jsonObject.addProperty("deckId", addTerms.getDeckId()))
				.build();
		}

		addTerms.getNewTerms().forEach(deck::putTerms);

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Terms to Deck!")
			.addObject("deck", deck)
			.build();
	}

	public String deleteDeck(long id) {
		Deck deck = service.getDeckById(id);

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addValue(jsonObject -> jsonObject.addProperty("deckId", id))
				.build();
		}

		service.deleteDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Deck!")
			.addObject("deletedDeck", deck)
			.build();
	}

	public String deleteTerms(DeckRequest.DeleteTerms deleteTerms) {
		Deck deck = service.getDeckById(deleteTerms.getDeckId());

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addValue(jsonObject -> jsonObject.addProperty("deckId", deleteTerms.getDeckId()))
				.build();
		}

		deleteTerms.getTermsToBeDeleted().forEach(deck::removeTerms);

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Terms From Deck!")
			.addObject("deck", deck)
			.build();
	}
}
