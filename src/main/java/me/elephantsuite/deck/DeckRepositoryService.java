package me.elephantsuite.deck;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import me.elephantsuite.ElephantBackendApplication;
import me.elephantsuite.deck.card.CardService;
import me.elephantsuite.folder.FolderRepository;
import me.elephantsuite.folder.FolderRepositoryService;
import me.elephantsuite.stats.ElephantUserStatisticsRepository;
import me.elephantsuite.user.ElephantUserRepository;
import me.elephantsuite.user.notification.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class DeckRepositoryService {

	private final DeckRepository deckRepository;

	private final FolderRepository folderRepository;

	private final NotificationRepository notificationRepository;

	private final ElephantUserRepository elephantUserRepository;

	private final ElephantUserStatisticsRepository elephantUserStatisticsRepository;

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
		return deckRepository.getAllDecks();
	}

	public void deleteDeck(Deck deck, CardService cardService) {
		deck.getCards().forEach(card -> {
			card.setDeck(null);
			cardService.saveCard(card);
		});

		deck.setCards(new ArrayList<>());

		deckRepository.deleteDeckById(deck.getId());

		folderRepository.deleteDeckFromFolder(deck.getId());

		notificationRepository.deleteNotificationDeckId(deck.getId());

    	elephantUserRepository.deleteLikedDecksFromUser(deck.getId());

		elephantUserRepository.deleteSharedDecksFromUser(deck.getId());

		elephantUserStatisticsRepository.deleteRecentlyViewedDeck(deck.getId());
	}
	public List<Deck> getDecksByUser(long userId) {
		return deckRepository.getDecksByUserId(userId);
	}

	public void saveAll(List<Deck> decks) {
		deckRepository.saveAll(decks);
	}

	public void deleteUserFromSharedUserIds(long userId) {
		deckRepository.deleteUserFromSharedDecks(userId);
	}
}
