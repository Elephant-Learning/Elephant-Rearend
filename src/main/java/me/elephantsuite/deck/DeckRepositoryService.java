package me.elephantsuite.deck;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeckRepositoryService {

	private final DeckRepository deckRepository;

	public Deck saveDeck(Deck deck) {
		return deckRepository.save(deck);
	}

	public Deck getDeckById(long id) {
		try {
			return deckRepository.getReferenceById(id);
		} catch (EntityNotFoundException e) {
			return null;
		} catch (Exception e) {
			ElephantBackendApplication.LOGGER.error("Error while getting deck by id!", e);
			e.printStackTrace();
			return null;
		}
	}

	public List<Deck> getAllDecks() {
		return deckRepository.findAll();
	}

	public void deleteDeck(Deck deck) {
		deckRepository.delete(deck);
	}
}
