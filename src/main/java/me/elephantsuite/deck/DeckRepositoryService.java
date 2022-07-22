package me.elephantsuite.deck;

import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeckRepositoryService {

	private final DeckRepository deckRepository;

	public Deck saveDeck(Deck deck) {
		return deckRepository.save(deck);
	}

	public Deck getDeckById(long id) {
		if (deckRepository.existsById(id)) {
			return deckRepository.getReferenceById(id);
		}

		return null;
	}

	public List<Deck> getAllDecks() {
		return deckRepository.findAll();
	}

	public void deleteDeck(Deck deck) {
		deckRepository.delete(deck);
	}
}
