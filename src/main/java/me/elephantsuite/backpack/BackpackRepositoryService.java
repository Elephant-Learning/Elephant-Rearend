package me.elephantsuite.backpack;

import java.util.List;

import lombok.AllArgsConstructor;
import me.elephantsuite.deck.Deck;
import me.elephantsuite.deck.DeckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class BackpackRepositoryService {

	private final BackpackRepository backpackRepository;

	public Backpack saveBackpack(Backpack backpack) {
		return backpackRepository.save(backpack);
	}

	public Backpack getBackpackById(long id) {
		if (backpackRepository.existsById(id)) {
			return backpackRepository.getReferenceById(id);
		}

		return null;
	}
}
