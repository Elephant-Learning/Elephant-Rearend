package me.elephantsuite.folder.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepositoryService;
import me.elephantsuite.folder.Folder;
import me.elephantsuite.folder.FolderRepositoryService;
import me.elephantsuite.registration.RegistrationService;
import me.elephantsuite.response.api.Response;
import me.elephantsuite.response.api.ResponseBuilder;
import me.elephantsuite.response.exception.InvalidIdException;
import me.elephantsuite.response.exception.InvalidIdType;
import me.elephantsuite.response.exception.InvalidTagInputException;
import me.elephantsuite.response.util.ResponseStatus;
import me.elephantsuite.response.util.ResponseUtil;
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

		ElephantUser user = ResponseUtil.checkUserValid(userId, userService);

		if (RegistrationService.isInvalidName(name)) {
			throw new InvalidTagInputException(name);
		}

		List<Deck> decks = deckIds
			.stream()
			.map(deckService::getDeckById)
			.collect(Collectors.toList());


		if (decks.contains(null)) {
			throw new InvalidIdException(createFolder, InvalidIdType.DECK);
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
			throw new InvalidIdException(removeDeck, InvalidIdType.DECK, InvalidIdType.FOLDER);
		}

		if (!folder.getDeckIds().contains(deckId)) {
			return ResponseUtil.getFailureResponse("Deck is not in that folder!", removeDeck);
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
			throw new InvalidIdException(addDeck, InvalidIdType.DECK, InvalidIdType.FOLDER);
		}

		if (folder.getDeckIds().contains(deckId)) {
			return ResponseUtil.getFailureResponse("Deck is already in that folder!", addDeck);
		}

		folder.getDeckIds().add(deckId);

		folder = service.save(folder);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Added Deck to Folder!")
			.addObject("folder", folder)
			.build();
	}

	public Response getFolderById(long id) {
		Folder folder = service.getFolderById(id);

		if (folder == null) {
			throw new InvalidIdException(id, InvalidIdType.FOLDER);
		}

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Retrieved Folder By ID!")
			.addObject("folder", folder)
			.build();
	}

	public Response setFolderName(FolderRequest.SetName setName) {
		long folderId = setName.getFolderId();
		String name = setName.getName();

		Folder folder = service.getFolderById(folderId);

		if (folder == null) {
			throw new InvalidIdException(setName, InvalidIdType.FOLDER);
		}

		if (RegistrationService.isInvalidName(name)) {
			throw new InvalidTagInputException(name);
		}

		folder.setName(name);

		service.save(folder);

		return ResponseBuilder
			.create()
			.addResponse(ResponseStatus.SUCCESS, "Set Folder Name!")
			.addObject("folder", folder)
			.build();
	}

    public Response deleteFolder(long id) {
		Folder folder = service.getFolderById(id);

		if (folder == null) {
			throw new InvalidIdException(id, InvalidIdType.FOLDER);
		}

		service.deleteFolder(folder);

		return ResponseBuilder
				.create()
				.addResponse(ResponseStatus.SUCCESS, "Deleted Folder!")
				.build();
    }
}
