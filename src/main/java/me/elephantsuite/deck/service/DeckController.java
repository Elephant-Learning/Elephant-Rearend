package me.elephantsuite.deck.service;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.response.Response;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@PostMapping(path = "visibility")
	public Response changeVisibility(@RequestBody DeckRequest.ChangeVisiblity changeVisiblity) {
		return deckService.changeVisibility(changeVisiblity);
	}

	@PostMapping(path = "shareDeck")
	public Response shareDeck(@RequestBody DeckRequest.ShareDeck shareDeck) {
		return deckService.shareDeck(shareDeck);
	}
}
