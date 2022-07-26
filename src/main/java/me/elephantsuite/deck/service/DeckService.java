package me.elephantsuite.deck.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.response.Response;
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

	private final CardService cardService;

	public Response createDeck(DeckRequest.CreateDeck request) {
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

		Deck deck = new Deck(null, user, name);

		List<Card> cards = convertToCards(terms, deck);

		deck.setCards(cards);

		user.getDecks().add(deck);

		cardService.saveAll(deck.getCards());

		deck = service.saveDeck(deck);

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Created Deck!")
			.addObject("user", user)
			.addObject("deck", deck)
			.build();
	}

	public static List<Card> convertToCards(Map<String, List<String>> cardsMap, Deck deck) {
		List<Card> cards = new ArrayList<>();

		cardsMap.forEach((s, strings) -> {
			Card card = new Card(s, strings, deck);
			cards.add(card);
		});

		return cards;
	}

	public Response likeDeck(DeckRequest.LikeDeck likeDeck) {
		Deck deck = service.getDeckById(likeDeck.getDeckId());

		ElephantUser user = userService.getUserById(likeDeck.getUserId());

		if (deck == null || user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck Or User ID!")
				.addObject("deckId", likeDeck.getDeckId())
				.addObject("userId", likeDeck.getUserId())
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

	public Response getAllDecks() {
		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved All Decks!")
			.addObject("decks", this.service.getAllDecks())
			.build();


	}

	public Response renameDeck(DeckRequest.RenameDeck renameDeck) {
		Deck deck = service.getDeckById(renameDeck.getDeckId());

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addObject("deckId", renameDeck.getDeckId())
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


	public Response resetTerms(DeckRequest.ResetTerms resetTerms) {
		Deck deck = service.getDeckById(resetTerms.getDeckId());

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addObject("deckId", resetTerms.getDeckId())
				.build();
		}

		deck.resetTerms(resetTerms.getNewTerms(), this.cardService);

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Terms to Deck!")
			.addObject("deck", deck)
			.build();
	}

	public Response deleteDeck(long id) {
		Deck deck = service.getDeckById(id);

		if (deck == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID!")
				.addObject("deckId", id)
				.build();
		}

		service.deleteDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Deck!")
			.build();
	}

}
