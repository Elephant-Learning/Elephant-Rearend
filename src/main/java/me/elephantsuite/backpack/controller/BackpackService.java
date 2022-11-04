package me.elephantsuite.backpack.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.backpack.BackpackRepositoryService;
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

		Card card = cardService.getCardById(cardId);

		ElephantUser user = userService.getUserById(userId);

		if (user == null || card == null) {
			throw new InvalidIdException(request, InvalidIdType.CARD, InvalidIdType.USER);
		}

		if (!user.isEnabled()) {
			throw new UserNotEnabledException(user);
		}

		if (user.getBackpack().getCards().contains(card)) {
			return ResponseUtil.getFailureResponse("Card already present in backpack!", request);
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

		Card card = cardService.getCardById(cardId);

		if (user == null || card == null) {
			throw new InvalidIdException(request, InvalidIdType.CARD, InvalidIdType.USER);
		}

		if (!user.isEnabled()) {
			throw new UserNotEnabledException(user);
		}

		if (!user.getBackpack().getCards().contains(card)) {
			return ResponseUtil.getFailureResponse("Card not present in backpack!", request);
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
