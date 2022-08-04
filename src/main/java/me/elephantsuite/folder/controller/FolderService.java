package me.elephantsuite.folder.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.backpack.BackpackRepositoryService;
import me.elephantsuite.backpack.controller.BackpackService;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.deck.card.Card;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.folder.Folder;
import me.elephantsuite.folder.FolderRepositoryService;
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
public class FolderService {

	private final FolderRepositoryService service;

	private final ElephantUserService userService;

	private final DeckRepositoryService deckService;

	public Response createFolder(FolderRequest.CreateFolder createFolder) {
		long userId = createFolder.getUserId();
		List<Long> deckIds = createFolder.getDeckIds();
		String name = createFolder.getName();

		ElephantUser user = userService.getUserById(userId);

		if (user == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid User ID!")
				.addObject("request", createFolder)
				.build();
		}

		List<Deck> decks = deckIds
			.stream()
			.map(deckService::getDeckById)
			.collect(Collectors.toList());


		if (decks.contains(null)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck ID found!")
				.addObject("request", createFolder)
				.addObject("user", user)
				.build();
		}

		//backpackService.initBackpack();

		Folder folder = new Folder(deckIds, user, name);

		if (!user.getFolders().contains(folder)) {
			user.getFolders().add(folder);
		}

		service.save(folder);

		user = userService.saveUser(user);


		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Successfully Created Folder!")
			.addObject("user", user)
			.addObject("decks", decks)
			.build();
	}

	public Response removeDeck(FolderRequest.RemoveDeck removeDeck) {
		long folderId = removeDeck.getFolderId();
		long deckId = removeDeck.getDeckId();

		Deck deck = deckService.getDeckById(deckId);
		Folder folder = service.getFolderById(folderId);

		if (deck == null || folder == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck or Folder ID!")
				.addObject("request", removeDeck)
				.build();
		}

		if (!folder.getDeckIds().contains(deckId)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Deck is not in that folder!")
				.addObject("deck", deck)
				.addObject("folder", folder)
				.build();
		}

		folder.getDeckIds().remove(deckId);

		folder = service.save(folder);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Removed Deck from Folder!")
			.addObject("folder", folder)
			.addObject("deck", deck)
			.build();

	}

	public Response addDeck(FolderRequest.AddDeck addDeck) {
		long folderId = addDeck.getFolderId();
		long deckId = addDeck.getDeckId();

		Deck deck = deckService.getDeckById(deckId);
		Folder folder = service.getFolderById(folderId);

		if (deck == null || folder == null) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Invalid Deck or Folder ID!")
				.addObject("request", addDeck)
				.build();
		}

		if (folder.getDeckIds().contains(deckId)) {
			return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.FAILURE, "Deck is already in that folder!")
				.addObject("deck", deck)
				.addObject("folder", folder)
				.build();
		}

		folder.getDeckIds().add(deckId);

		folder = service.save(folder);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Deck to Folder!")
			.addObject("folder", folder)
			.build();
	}
}
