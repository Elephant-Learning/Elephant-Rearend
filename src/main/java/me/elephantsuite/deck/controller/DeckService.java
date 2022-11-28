package me.elephantsuite.deck.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.DeckVisibility;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.UserNotEnabledException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
import me.elephantsuite.user.ElephantUser;
import me.elephantsuite.user.ElephantUserService;
import org.apache.commons.lang3.StringUtils;
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
		DeckVisibility visibility = request.getVisibility();

		ElephantUser user = ResponseUtil.checkUserValid(authorId, userService);

		Deck deck = new Deck(null, user, name, visibility);

		List<Card> cards = convertToCards(terms, deck ,cardService);

		deck.setCards(cards);

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

	public static List<Card> convertToCards(Map<String, List<String>> cardsMap, Deck deck, CardService cardService) {
		List<Card> cards = new ArrayList<>();

		cardsMap.forEach((s, strings) -> {
			Card card = new Card(s, strings, deck);
			cards.add(card);
			cardService.saveCard(card);
		});

		return cards;
	}

	public Response likeDeck(DeckRequest.LikeDeck likeDeck) {
		Deck deck = checkDeck(likeDeck.getDeckId());

		ElephantUser user = ResponseUtil.checkUserValid(likeDeck.getUserId(), userService);

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
		Deck deck = checkDeck(renameDeck.getDeckId());

		deck.setName(renameDeck.getNewName());

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Renamed Deck!")
			.addObject("deck", deck)
			.build();
	}


	public Response resetTerms(DeckRequest.ResetTerms resetTerms) {
		Deck deck = checkDeck(resetTerms.getDeckId());

		deck.resetTerms(resetTerms.getNewTerms(), this.cardService);

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Terms to Deck!")
			.addObject("deck", deck)
			.build();
	}

	public Response deleteDeck(long id) {
		Deck deck = checkDeck(id);

		service.deleteDeck(deck, cardService);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Deck!")
			.build();
	}

	public Response changeVisibility(DeckRequest.ChangeVisiblity changeVisiblity) {
		long id = changeVisiblity.getDeckId();
		DeckVisibility visibility = changeVisiblity.getVisibility();

		Deck deck = checkDeck(id);

		deck.setVisibility(visibility);

		List<ElephantUser> sharedUsers = deck.getSharedUsersIds()
			.stream()
			.map(userService::getUserById)
			.toList();

		for (ElephantUser elephantUser : sharedUsers) {
			elephantUser.getSharedDeckIds().remove(deck.getId());
			userService.saveUser(elephantUser);
		}

		deck.getSharedUsersIds().clear();

		deck = service.saveDeck(deck);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Changed Visibility of Deck!")
			.addObject("deck", deck)
			.build();
	}

	//use same as share deck bc lazy
	public Response unshareDeck(DeckRequest.ShareDeck shareDeck) {
		long userId = shareDeck.getSharedUserId();
		long deckId = shareDeck.getDeckId();

		Deck deck = checkDeck(deckId);
		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		if (!deck.getSharedUsersIds().contains(userId) || !user.getSharedDeckIds().contains(deckId)) {
			return ResponseUtil.getFailureResponse("Deck and user are not shared with each other!", shareDeck);
		}

		deck.getSharedUsersIds().remove(userId);
		user.getSharedDeckIds().remove(deckId);

		deck = service.saveDeck(deck);
		user = userService.saveUser(user);

		return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Unshared Deck from User!")
				.addObject("deck", deck)
				.addObject("user", user)
				.build();
	}

	public Response shareDeck(DeckRequest.ShareDeck shareDeck) {
		long userId = shareDeck.getSharedUserId();
		long deckId = shareDeck.getDeckId();

		Deck deck = checkDeck(deckId);
		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		if (user.equals(deck.getAuthor())) {
			return ResponseUtil.getFailureResponse("Cannot Share Own Deck With Yourself!", shareDeck);
		}

		if (deck.getVisibility().equals(DeckVisibility.PRIVATE)) {
			return ResponseUtil.getFailureResponse("Cannot share a deck that is private!", shareDeck);
		}

		if (deck.getSharedUsersIds().contains(userId) || user.getSharedDeckIds().contains(deckId)) {
			return ResponseUtil.getFailureResponse("Deck and user are already shared!", shareDeck);
		}

		deck.getSharedUsersIds().add(userId);
		user.getSharedDeckIds().add(deckId);

		deck = service.saveDeck(deck);
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Shared Deck with User!")
			.addObject("deck", deck)
			.addObject("sharedUser", user)
			.build();
	}

	public Response getById(long id) {
		Deck deck = checkDeck(id);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved Deck By ID!")
			.addObject("deck", deck)
			.build();
	}

	public Response unlikeDeck(DeckRequest.LikeDeck likeDeck) {
		Deck deck = checkDeck(likeDeck.getDeckId());

		ElephantUser user = ResponseUtil.checkUserValid(likeDeck.getUserId(), userService);

		deck.unlikeDeck();

		deck = service.saveDeck(deck);

		user.getLikedDecksIds().remove(likeDeck.getDeckId());

		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Unliked Deck!")
			.addObject("deck", deck)
			.addObject("user", user)
			.build();
	}

	public Response getByName(long userId, String name) {

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		List<Deck> filteredDecks = service
			.getAllDecks()
			.stream()
			.filter(deck -> StringUtils.containsIgnoreCase(deck.getName(), name))
			.filter(deck -> deck.getVisibility().equals(DeckVisibility.PUBLIC) || (deck.getVisibility().equals(DeckVisibility.SHARED) && deck.getSharedUsersIds().contains(userId)) || deck.getAuthor().equals(user))
			.collect(Collectors.toList());

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved Decks with Name!")
			.addObject("decks", filteredDecks)
			.build();
	}

	public Response deleteCard(long cardId) {
		Card card = cardService.getCardById(cardId);

		if (card == null) {
			throw new InvalidIdException(cardId, InvalidIdType.CARD);
		}

		cardService.deleteCardById(cardId);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Deleted Card!")
			.build();
	}

	public Deck checkDeck(long id) {
		Deck deck = service.getDeckById(id);

		if (deck == null) {
			throw new InvalidIdException(id, InvalidIdType.DECK);
		}

		return deck;
	}
}
