package me.elephantsuite.deck.controller;

import lombok.AllArgsConstructor;
import me.elephantsuite.response.api.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "deck")
@AllArgsConstructor
public class DeckController {

	private final DeckService deckService;

	@PostMapping(path = "create")
	public Response createDeck(@RequestBody DeckRequest.CreateDeck request) {
		return deckService.createDeck(request);
	}

	@PostMapping(path = "like")
	public Response likeDeck(@RequestBody DeckRequest.LikeDeck likeDeck) {
		return deckService.likeDeck(likeDeck);
	}

	@PostMapping(path = "unlike")
	public Response unlikeDeck(@RequestBody DeckRequest.LikeDeck likeDeck) {
		return deckService.unlikeDeck(likeDeck);
	}

	@GetMapping(path = "getByName")
	public Response getByName(@RequestParam("userId") long id, @RequestParam("name") String name) {
		return deckService.getByName(id, name);
	}

	@GetMapping(path = "getAll")
	public Response getAllDecks() {
		return deckService.getAllDecks();
	}

	@PostMapping(path = "rename")
	public Response renameDeck(@RequestBody DeckRequest.RenameDeck renameDeck) {
		return deckService.renameDeck(renameDeck);
	}

	@PostMapping(path = "resetTerms")
	public Response resetTerms(@RequestBody DeckRequest.ResetTerms resetTerms) {
		return deckService.resetTerms(resetTerms);
	}


	@DeleteMapping(path = "delete")
	public Response deleteDeck(@RequestParam("id") long id) {
		return deckService.deleteDeck(id);
	}

	@DeleteMapping(path = "deleteCard")
	public Response deleteCard(@RequestParam("cardId") long cardId) {
		return deckService.deleteCard(cardId);
	}

	@PostMapping(path = "visibility")
	public Response changeVisibility(@RequestBody DeckRequest.ChangeVisiblity changeVisiblity) {
		return deckService.changeVisibility(changeVisiblity);
	}

	@PostMapping(path = "shareDeck")
	public Response shareDeck(@RequestBody DeckRequest.ShareDeck shareDeck) {
		return deckService.shareDeck(shareDeck);
	}

	@PostMapping("unshareDeck")
	public Response unshareDeck(@RequestBody DeckRequest.ShareDeck shareDeck) {
		return deckService.unshareDeck(shareDeck);
	}

	@GetMapping(path = "get")
	public Response getDeckById(@RequestParam("id") long id) {
		return deckService.getById(id);
	}
}
