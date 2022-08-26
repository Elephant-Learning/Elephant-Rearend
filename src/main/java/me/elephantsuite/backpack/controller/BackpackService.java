package me.elephantsuite.backpack.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.backpack.BackpackRepositoryService;
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
@Transactional
@AllArgsConstructor
public class BackpackService {

	private final ElephantUserService userService;

	private final CardService cardService;

	private final BackpackRepositoryService backpackService;

	public Response addCard(BackpackRequest.AddCard request) {
		long userId = request.getUserId();
		long cardId = request.getCardId();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Author ID Given!")
				.addObject("request", request)
				.build();
		}

		if (!user.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User not enabled!")
				.addObject("user", user)
				.build();
		}

		Card card = cardService.getCardById(cardId);

		if (card == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Card ID Given!")
				.addObject("request", request)
				.build();
		}

		if (user.getBackpack().getCards().contains(card)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Card already present in backpack!")
				.addObject("user", user)
				.build();
		}

		user.getBackpack().getCards().add(card);



		backpackService.saveBackpack(user.getBackpack());
		cardService.saveCard(card);
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Card to Backpack!")
			.addObject("user", user)
			.build();
	}

	public Response removeCard(BackpackRequest.RemoveCard request) {
		long userId = request.getUserId();
		long cardId = request.getCardId();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Author ID Given!")
				.addObject("request", request)
				.build();
		}

		if (!user.isEnabled()) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "User not enabled!")
				.addObject("user", user)
				.build();
		}

		Card card = cardService.getCardById(cardId);

		if (card == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Card ID Given!")
				.addObject("request", request)
				.build();
		}

		if (!user.getBackpack().getCards().contains(card)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Card not present in backpack!")
				.addObject("user", user)
				.build();
		}

		user.getBackpack().getCards().remove(card);


		backpackService.saveBackpack(user.getBackpack());
		cardService.saveCard(card);
		user = userService.saveUser(user);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Removed Card from Backpack!")
			.addObject("user", user)
			.build();
	}
}
